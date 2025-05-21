package com.uro.serialport.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.uro.serialport.adapter.RfidCodesAdapter;
import com.uro.serialport.model.Coleta;
import com.urovo.serialport.R;
import com.urovo.serialport.databinding.ActivityDetalhesColetaBinding;

import java.io.Serializable;

/**
 * Activity para exibir os detalhes de uma coleta RFID
 */
public class DetalhesColetaActivity extends BaseActivity {
    
    private static final String EXTRA_COLETA = "extra_coleta";
    
    private ActivityDetalhesColetaBinding mBinding;
    private RfidCodesAdapter mAdapter;
    private Coleta mColeta;
    
    /**
     * Método estático para iniciar a activity
     * @param context Contexto
     * @param coleta Coleta a ser exibida
     */
    public static void start(Context context, Coleta coleta) {
        Intent intent = new Intent(context, DetalhesColetaActivity.class);
        intent.putExtra(EXTRA_COLETA, (Serializable) coleta);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalhes_coleta);
        
        // Obtém a coleta passada como parâmetro
        mColeta = (Coleta) getIntent().getSerializableExtra(EXTRA_COLETA);
        if (mColeta == null) {
            showDialogError("Dados da coleta não encontrados");
            finish();
            return;
        }
        
        initViews();
        displayColetaDetails();
    }
    
    /**
     * Inicializa as views e listeners
     */
    private void initViews() {
        // Configurar RecyclerView
        mAdapter = new RfidCodesAdapter();
        mBinding.rvDetalhesCodigos.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvDetalhesCodigos.setAdapter(mAdapter);
        
        // Configurar botão de voltar
        mBinding.btnDetalhesVoltar.setOnClickListener(v -> finish());
    }
    
    /**
     * Exibe os detalhes da coleta na interface
     */
    private void displayColetaDetails() {
        // Exibe informações da coleta
        mBinding.tvDetalhesInventario.setText("Inventário: " + mColeta.getInventarioId());
        mBinding.tvDetalhesLoja.setText("Loja: " + mColeta.getLojaNome());
        mBinding.tvDetalhesLocal.setText("Local: " + mColeta.getLocalColeta());
        mBinding.tvDetalhesData.setText("Data: " + mColeta.getDataHoraColeta());
        mBinding.tvDetalhesTotal.setText("Total de itens: " + mColeta.getTotalCodigos());
        
        // Adiciona os códigos RFID ao adaptador
        if (mColeta.getCodigosRfid() != null) {
            for (int i = 0; i < mColeta.getCodigosRfid().size(); i++) {
                mAdapter.addRfidCode(mColeta.getCodigosRfid().get(i));
            }
        }
    }
}
