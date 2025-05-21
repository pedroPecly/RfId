package com.uro.serialport.view.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uro.serialport.adapter.ColetasAdapter;
import com.uro.serialport.model.Coleta;
import com.uro.serialport.util.ColetaManager;
import com.urovo.serialport.R;
import com.urovo.serialport.databinding.ActivityHistoricoColetasBinding;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity para exibir o histórico de coletas RFID
 */
public class HistoricoColetasActivity extends BaseActivity implements ColetasAdapter.OnItemClickListener {
    
    private ActivityHistoricoColetasBinding mBinding;
    private ColetasAdapter mAdapter;
    private List<Coleta> mColetas;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_historico_coletas);
        
        initViews();
        loadColetas();
    }
    
    /**
     * Inicializa as views e listeners
     */
    private void initViews() {
        // Configurar RecyclerView
        mAdapter = new ColetasAdapter(this);
        mBinding.rvColetas.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvColetas.setAdapter(mAdapter);
        
        // Configurar botões
        mBinding.btnVoltar.setOnClickListener(v -> finish());
        mBinding.btnExportar.setOnClickListener(v -> exportarColetas());
    }
    
    /**
     * Carrega a lista de coletas do armazenamento
     */
    private void loadColetas() {
        mColetas = ColetaManager.getInstance(this).getColetas();
        mAdapter.setColetas(mColetas);
        
        // Mostra mensagem se não houver coletas
        if (mColetas.isEmpty()) {
            showToast(getString(R.string.historico_sem_coletas));
        }
    }
    
    /**
     * Exporta a lista de coletas para um arquivo JSON
     */
    private void exportarColetas() {
        if (mColetas == null || mColetas.isEmpty()) {
            showToast(getString(R.string.historico_sem_coletas));
            return;
        }
        
        showToast(getString(R.string.historico_exportando));
        
        new Thread(() -> {
            try {
                // Cria nome do arquivo com timestamp
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String timestamp = sdf.format(new Date());
                String fileName = "coletas_" + timestamp + ".json";
                
                // Obtém diretório de Downloads
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File exportFile = new File(downloadsDir, fileName);
                
                // Converte lista para JSON e salva
                Gson gson = new Gson();
                String json = gson.toJson(mColetas);
                
                FileWriter writer = new FileWriter(exportFile);
                writer.write(json);
                writer.close();
                
                runOnUiThread(() -> 
                    showToast(getString(R.string.historico_exportado) + ": " + exportFile.getAbsolutePath())
                );
                
            } catch (Exception e) {
                runOnUiThread(() -> 
                    showDialogError("Erro ao exportar: " + e.getMessage())
                );
            }
        }).start();
    }
    
    @Override
    public void onItemClick(Coleta coleta) {
        // Abre tela de detalhes da coleta
        DetalhesColetaActivity.start(this, coleta);
    }
}
