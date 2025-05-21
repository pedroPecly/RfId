package com.uro.serialport.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.uro.serialport.adapter.RfidCodesAdapter;
import com.uro.serialport.model.Coleta;
import com.uro.serialport.model.RfidCode;
import com.uro.serialport.util.ColetaManager;
import com.uro.utils.DateUtil;
import com.urovo.rfid.RfidReaderMange;
import com.urovo.rfid.RfidReaderMangeInventoryCallBack;
import com.urovo.serial.common.GlobalConstant;
import com.urovo.serialport.R;
import com.urovo.serialport.databinding.ActivityColetaBinding;

import java.util.Date;
import java.util.Map;

/**
 * Activity para coleta de dados RFID em inventário.
 * Baseada no SDK já existente.
 *
 * Objetivo:
 * - Mostrar inventário e loja
 * - Selecionar local da coleta (Spinner)
 * - Ler códigos RFID com o SDK e exibir em tempo real
 * - Botão para processar e salvar a coleta
 * 
 * Reutiliza:
 * - RfidReaderMange
 * - SerialPortTool
 * 
 * Os dados são armazenados localmente usando SharedPreferences e Gson.
 */
public class ColetaActivity extends BaseActivity {
    // Constantes
    private static final String EXTRA_INVENTARIO_ID = "extra_inventario_id";
    private static final String EXTRA_LOJA_NOME = "extra_loja_nome";
    
    // Binding e UI
    private ActivityColetaBinding mBinding;
    private RfidCodesAdapter mAdapter;
    
    // Gerenciamento RFID
    private RfidReaderMange mRfidReaderMange;
    private boolean mIsReading = false;
    
    // Dados da coleta
    private String mInventarioId;
    private String mLojaNome;
    private String mLocalColeta;
    
    // Callback para receber os dados de inventário RFID
    private final RfidReaderMangeInventoryCallBack mInventoryCallback = new RfidReaderMangeInventoryCallBack() {
        @Override
        public void onInventoryTag(Map<String, Object> respMap) {
            if (isFinishing() || isDestroyed() || respMap == null) return;
            
            try {
                // Obtém o EPC lido e adiciona ao adaptador
                String epc = (String) respMap.get(GlobalConstant.RespDataKey.RFID_InventoryEpc);
                if (epc != null && !epc.isEmpty()) {
                    String timestamp = DateUtil.getDateTime2(new Date());
                    RfidCode code = new RfidCode(epc, timestamp);
                    
                    runOnUiThread(() -> {
                        if (mAdapter.addRfidCode(code)) {
                            // Atualiza o contador total
                            mBinding.tvTotalItens.setText("Total: " + mAdapter.getItemCount() + " itens");
                            // Rola a lista para o último item adicionado
                            mBinding.rvCodigosRfid.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    });
                }
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao processar tag: " + e.getMessage());
            }
        }
        
        @Override
        public void onInventoryStatus(String status) {
            if (isFinishing() || isDestroyed()) return;
            
            runOnUiThread(() -> {
                mBinding.tvStatusLeitura.setText("Status: " + status);
            });
        }
        
        @Override
        public void onFail(String errorCode, String msg) {
            if (isFinishing() || isDestroyed()) return;
            
            outputColorText(TextColor.RED, DateUtil.getDateTime1(new Date()) + ">>" + 
                    "Erro: " + errorCode + ": " + msg);
        }
    };
    
    /**
     * Cria um intent para iniciar a ColetaActivity
     * @param context Contexto
     * @param inventarioId ID do inventário
     * @param lojaNome Nome da loja
     * @return Intent configurado
     */
    public static android.content.Intent createIntent(Context context, String inventarioId, String lojaNome) {
        android.content.Intent intent = new android.content.Intent(context, ColetaActivity.class);
        intent.putExtra(EXTRA_INVENTARIO_ID, inventarioId);
        intent.putExtra(EXTRA_LOJA_NOME, lojaNome);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_coleta);
        
        // Obtém parâmetros da intent
        mInventarioId = getIntent().getStringExtra(EXTRA_INVENTARIO_ID);
        mLojaNome = getIntent().getStringExtra(EXTRA_LOJA_NOME);
        
        // Define os dados de binding
        mBinding.setInventarioId(mInventarioId);
        mBinding.setLojaNome(mLojaNome);
        
        // Inicializa componentes e RFID
        initRfidManager();
        initViews();
    }
    
    /**
     * Inicializa o gerenciador RFID
     */
    private void initRfidManager() {
        try {
            mRfidReaderMange = RfidReaderMange.getInstance();
            int result = mRfidReaderMange.initialize(this);
            
            if (result == GlobalConstant.SerialPortResp.Success) {
                outputColorText(TextColor.GREEN, "Inicialização do RFID bem-sucedida");
            } else {
                outputColorText(TextColor.RED, "Falha na inicialização do RFID: " + 
                        mRfidReaderMange.getErrorMessage(result));
                showDialogError("Falha ao inicializar o leitor RFID. Por favor, verifique a conexão.");
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro na inicialização do RFID: " + e.getMessage());
            showDialogError("Erro ao inicializar o leitor RFID: " + e.getMessage());
        }
    }
    
    @Override
    public void initView() {
        super.initView();
        
        // Inicializa o RecyclerView
        mAdapter = new RfidCodesAdapter();
        mBinding.rvCodigosRfid.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvCodigosRfid.setAdapter(mAdapter);
        
        // Configura o Spinner para locais de coleta
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.locais_coleta, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerLocalColeta.setAdapter(spinnerAdapter);
        mBinding.spinnerLocalColeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLocalColeta = parent.getItemAtPosition(position).toString();
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLocalColeta = null;
            }
        });
        
        // Configura os botões
        setupButtons();
    }
    
    /**
     * Inicializa as views e listeners
     */
    private void initViews() {
        initView(); // Chama o método da superclasse
        
        // Inicializa o estado da interface
        mBinding.tvStatusLeitura.setText("Status: Aguardando leitura");
        mBinding.tvTotalItens.setText("Total: 0 itens");
    }
    
    /**
     * Configura os botões da interface
     */
    private void setupButtons() {
        // Botão Voltar
        mBinding.btnVoltar.setOnClickListener(v -> finish());
        
        // Botão Ler Códigos
        mBinding.btnLerCodigos.setOnClickListener(v -> {
            if (mIsReading) {
                stopReading();
            } else {
                startReading();
            }
        });
        
        // Botão Processar
        mBinding.btnProcessar.setOnClickListener(v -> {
            // Verifica se há códigos lidos
            if (mAdapter.getItemCount() == 0) {
                showMessage("Nenhum código RFID foi lido. Inicie a leitura antes de processar.");
                return;
            }
            
            // Verifica se um local foi selecionado
            if (mLocalColeta == null || mLocalColeta.isEmpty()) {
                showMessage("Selecione um local de coleta antes de processar.");
                return;
            }
            
            // Interrompe a leitura se estiver ativa
            if (mIsReading) {
                stopReading();
            }
            
            // Confirma o processamento
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Processamento")
                    .setMessage("Deseja processar esta coleta com " + mAdapter.getItemCount() + " itens?")
                    .setPositiveButton("Processar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            processarColeta();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
    
    /**
     * Inicia a leitura dos códigos RFID
     */
    private void startReading() {
        try {
            if (mRfidReaderMange != null) {
                int result = mRfidReaderMange.startInventory(mInventoryCallback);
                
                if (result == GlobalConstant.SerialPortResp.Success) {
                    mIsReading = true;
                    mBinding.btnLerCodigos.setText("Parar Leitura");
                    mBinding.tvStatusLeitura.setText("Status: Leitura em andamento...");
                    outputColorText(TextColor.GREEN, "Leitura RFID iniciada");
                } else {
                    outputColorText(TextColor.RED, "Falha ao iniciar leitura: " + 
                            mRfidReaderMange.getErrorMessage(result));
                }
            } else {
                outputColorText(TextColor.RED, "Gerenciador RFID não inicializado");
                showDialogError("Gerenciador RFID não inicializado. Tente reiniciar o aplicativo.");
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro ao iniciar leitura: " + e.getMessage());
            showDialogError("Erro ao iniciar leitura: " + e.getMessage());
        }
    }
    
    /**
     * Interrompe a leitura dos códigos RFID
     */
    private void stopReading() {
        try {
            if (mRfidReaderMange != null) {
                int result = mRfidReaderMange.stopInventory();
                
                if (result == GlobalConstant.SerialPortResp.Success) {
                    mIsReading = false;
                    mBinding.btnLerCodigos.setText("Ler Códigos");
                    mBinding.tvStatusLeitura.setText("Status: Leitura interrompida");
                    outputColorText(TextColor.GREEN, "Leitura RFID interrompida");
                } else {
                    outputColorText(TextColor.RED, "Falha ao interromper leitura: " + 
                            mRfidReaderMange.getErrorMessage(result));
                }
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro ao interromper leitura: " + e.getMessage());
        }
    }
    
    /**
     * Processa e salva a coleta atual
     */
    private void processarColeta() {
        try {
            // Cria objeto da coleta
            String timestamp = DateUtil.getDateTime1(new Date());
            Coleta coleta = new Coleta(mInventarioId, mLojaNome, mLocalColeta, timestamp);
            coleta.setCodigosRfid(mAdapter.getRfidCodes());
            
            // Salva no gerenciador de coletas
            boolean success = ColetaManager.getInstance(this).saveColeta(coleta);
            
            if (success) {
                // Exibe mensagem de sucesso
                showToast("Coleta processada com sucesso! " + coleta.getTotalCodigos() + " itens salvos.");
                
                // Limpa a lista de códigos lidos
                mAdapter.clearRfidCodes();
                mBinding.tvTotalItens.setText("Total: 0 itens");
                
                // Redireciona para a tela anterior após pequeno delay
                mBinding.getRoot().postDelayed(() -> finish(), 1500);
            } else {
                showDialogError("Erro ao salvar coleta. Tente novamente.");
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro ao processar coleta: " + e.getMessage());
            showDialogError("Erro ao processar coleta: " + e.getMessage());
        }
    }
    
    @Override
    protected void onDestroy() {
        // Interrompe a leitura se estiver ativa
        if (mIsReading && mRfidReaderMange != null) {
            try {
                mRfidReaderMange.stopInventory();
            } catch (Exception e) {
                // Ignora erro ao fechar
            }
        }
        
        // Libera recursos do RFID
        releaseRfidResources();
        
        super.onDestroy();
    }
    
    /**
     * Libera recursos do RFID
     */
    private void releaseRfidResources() {
        try {
            if (mRfidReaderMange != null) {
                mRfidReaderMange.release();
            }
        } catch (Exception e) {
            // Ignora erro ao liberar recursos
        }
    }
}
