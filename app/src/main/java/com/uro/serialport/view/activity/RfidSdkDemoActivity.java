package com.uro.serialport.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.uro.utils.DateUtil;
import com.urovo.rfid.RfidReaderMange;
import com.urovo.rfid.RfidReaderMangeModuleInfoCallBack;
import com.urovo.serial.common.GlobalConstant;
import com.urovo.serialport.R;
import com.urovo.serialport.databinding.ActivityRfidSdkDemoBinding;

import java.util.Date;
import java.util.Map;

/**
 * Activity principal para demonstração do SDK RFID
 * @author ZhengJuE
 * @date 2023-01-11
 */
public class RfidSdkDemoActivity extends BaseActivity {
    private ActivityRfidSdkDemoBinding mBinding;
    private RfidReaderMange mRfidReaderMange;
    
    // Callback para receber informações do módulo RFID
    private final RfidReaderMangeModuleInfoCallBack mangeModuleInfoCallBack = new RfidReaderMangeModuleInfoCallBack() {
        @Override
        public void onReadBatteryInfo(Map<String, Object> respMap) {
            if (isFinishing() || isDestroyed() || respMap == null) return;
            
            try {
                int batteryStatus = (int) respMap.get(GlobalConstant.RespDataKey.BatteryStatus);
                Object batteryLevel = respMap.get(GlobalConstant.RespDataKey.BatteryLevel);
                
                if (batteryLevel != null) {
                    outputColorText(TextColor.GREEN, DateUtil.getDateTime1(new Date()) + ">>" + 
                            GlobalConstant.RespDataKey.BatteryLevel + ": " + batteryLevel + "%");
                }
                
                outputColorText(TextColor.GREEN, DateUtil.getDateTime1(new Date()) + ">>" + 
                        GlobalConstant.RespDataKey.BatteryStatus + ": " + batteryStatus + 
                        "（" + (batteryStatus == 1 ? "carregando" : "não carregando") + ")");
                
                updateStatus();
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao processar informações da bateria: " + e.getMessage());
            }
        }        @Override
        public void onModuleStatus(String status) {
            if (isFinishing() || isDestroyed()) return;
            
            runOnUiThread(() -> {
                if (mBinding != null) {
                    mBinding.tvModuleStatus.setText("Status do Módulo: " + status);
                }
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
     * Atualiza as informações de status na interface
     */
    private void updateStatus() {
        if (isFinishing() || isDestroyed() || mBinding == null || mRfidReaderMange == null) return;
        
        runOnUiThread(() -> {
            try {
                String statusText = mRfidReaderMange.getCurrentPathName() + " " + mRfidReaderMange.getCurrentSpeed();
                mBinding.tvStatus.setText(statusText);
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao atualizar status: " + e.getMessage());
            }
        });
    }    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rfid_sdk_demo);
        
        initRfidManager();
        initView();
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
                updateStatus();
            } else {
                outputColorText(TextColor.RED, "Falha na inicialização do RFID: " + 
                        mRfidReaderMange.getErrorMessage(result));
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro na inicialização do RFID: " + e.getMessage());
        }
    }

    @Override
    public void initView() {
        super.initView();
        
        // Configurar o botão de inicialização
        mBinding.btnInit.setOnClickListener(view -> {
            try {
                int result = mRfidReaderMange.initialize(RfidSdkDemoActivity.this);
                outputText(getString(R.string.initialize) + ": " + result + " " + 
                        mRfidReaderMange.getErrorMessage(result));
                
                if (result == GlobalConstant.SerialPortResp.Success) {
                    updateStatus();
                }
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao inicializar: " + e.getMessage());
            }
        });        // Configurar o botão de solicitação de informações
        mBinding.btnRequest.setOnClickListener(view -> {
            outputText(getString(R.string.active_read_battery_info));
            try {
                if (mRfidReaderMange != null) {
                    mRfidReaderMange.startMonitorModuleInfo(5, mangeModuleInfoCallBack);
                } else {
                    outputColorText(TextColor.RED, "Gerenciador RFID não inicializado");
                }
            } catch (Exception e) {
                e.printStackTrace();
                outputColorText(TextColor.RED, getString(R.string.active_read_battery_info) + 
                        " Exceção: " + e.toString());
            }
        });

        // Configurar o botão de leitura do modelo
        mBinding.btnReaderModel.setOnClickListener(view -> {
            try {
                if (mRfidReaderMange != null) {
                    String model = mRfidReaderMange.getRfidModel();
                    outputText(getString(R.string.rfid_reader_model) + ": " + model);
                } else {
                    outputColorText(TextColor.RED, "Gerenciador RFID não inicializado");
                }
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao obter modelo: " + e.getMessage());
            }
        });

        // Configurar o botão de liberação
        mBinding.btnRelease.setOnClickListener(view -> {
            try {
                if (mRfidReaderMange != null) {
                    mRfidReaderMange.release();
                    outputText("Liberação concluída");
                    mBinding.tvStatus.setText(getString(R.string.disconnect));
                } else {
                    outputColorText(TextColor.RED, "Gerenciador RFID não inicializado");
                }
            } catch (Exception e) {
                outputColorText(TextColor.RED, "Erro ao liberar recursos: " + e.getMessage());
            }
        });
    }    @Override
    protected void onDestroy() {
        // Liberar recursos do RFID antes de destruir a activity
        releaseRfidResources();
        super.onDestroy();
    }
    
    /**
     * Libera recursos do RFID de forma segura
     */
    private void releaseRfidResources() {
        try {
            if (mRfidReaderMange != null) {
                mRfidReaderMange.release();
                outputText("Recursos RFID liberados com sucesso");
            }
        } catch (Exception e) {
            outputColorText(TextColor.RED, "Erro ao liberar recursos RFID: " + e.getMessage());
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Opcional: parar o monitoramento quando a activity não está visível
        try {
            if (mRfidReaderMange != null) {
                // Se houver algum método para parar o monitoramento, chamar aqui
            }
        } catch (Exception e) {
            // Apenas log, não interferir na navegação
        }
    }
}
