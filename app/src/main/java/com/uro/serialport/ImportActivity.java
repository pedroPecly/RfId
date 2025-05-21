package com.uro.serialport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.uro.utils.ByteUtil;
import com.uro.utils.ConfigurationUtils;
import com.urovo.serial.utils.SerialPortListener;
import com.urovo.serial.utils.SerialPortTool;
import com.urovo.serialport.R;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ImportActivity.class.getSimpleName();    private TextView mTvSend;//dados enviados
    private TextView mTvReceive;//dados realmente recebidos
    private TextView mTvResult;//dados processados
    private EditText mEdSend;//campo de entrada
    private Button mBtnSend,settingSerPort;
    private SerialPortTool mSerialPortTool;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        setTitle(getResources().getString(R.string.app_name)+" v"+getAppInfo()+"       Transferência POS");
    
        mTvSend = findViewById(R.id.tv_send);
        mTvReceive = findViewById(R.id.tv_receive);
        mTvResult = findViewById(R.id.tv_result);
        mEdSend = findViewById(R.id.ed_send);
        mBtnSend = findViewById(R.id.button);
        mBtnSend.setOnClickListener(this);
        settingSerPort =  findViewById(R.id.btn_setting_sp);
        settingSerPort.setOnClickListener(this);
        initViewData();
    }
    
    
    private void initViewData() {
        
        mEdSend.setText("0123456789ABCDEF");
    }
    
    
    private String getSendTextView() {
        
        String str =  mEdSend.getText().toString().trim();
        return  str  ;
    }

      private void sendData() {
    
        try {
            String sendStr = getSendTextView();

            mTvSend.setText("Dados enviados: " + sendStr);
            String str1 = sendStr.replace("x","0");
            String str2 = str1.replace("X","0");

            byte[] sendData = ByteUtil.hexString2Bytes(str2);
//            byte[] byteData = sendStr.getBytes("UTF-8");
//            String str = ByteUtil.bytes2HexString(byteData);
//            byte[] sendData = ByteUtil.hexString2Bytes(str);
            String result = mSerialPortTool.sendData(sendData, sendData.length);
            if (result != null) {
                toast(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
    StringBuffer receiveSBuffer = new StringBuffer();
    StringBuffer resultsBuffer = new StringBuffer();
    SerialPortListener listener = new SerialPortListener() {
        
        @Override
        public void onReceive(byte[] data) {

            String hexStr = ByteUtil.bytes2HexString(data);
            byte[] byteData = hexStr.getBytes();
            String result = null;
            try {
                result = new String(byteData,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


//            String result = null;//ByteUtil.bytes2HexString(data);
//            try {
//                result = new String(data,"UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                Log.e("SerialPort","接收串口数据异常"+e.getMessage());
//            }
              receiveSBuffer.append(result + "");
            mTvReceive.setText("Recebido: \n" + receiveSBuffer.toString());
    
            resultsBuffer.append(result);
            if (resultsBuffer.toString().indexOf(".") > 0) {
                String sub = resultsBuffer.substring(resultsBuffer.indexOf(",") + 1, resultsBuffer.indexOf("."));
                mTvResult.setText("Dados válidos recebidos: \n" + sub);
                clearData();
            }
            
        }

        @Override
        public void onFail(String code, String msg) {

        }
    };

    
      /**
     * Limpar dados do StringBuffer
     */
    private void clearData() {

//        receiveSBuffer.setLength(0);
        resultsBuffer.setLength(0);
    }
    
    
    @Override
    protected void onResume() {
        
        super.onResume();
        String serialStr = ConfigurationUtils.getSerialPort_Value(0);
        int baudrates = ConfigurationUtils.getBaudrates_Value(0);
        Log.e(TAG,"serialStr:"+serialStr + "baudrates:"+baudrates);
        mSerialPortTool = new SerialPortTool(serialStr,baudrates);
        String result = mSerialPortTool.open();
        if (result != null) {
            DisplayError(result);
            return;
        }
        mSerialPortTool.setOnListener(listener);
    }
    
    
    private void toast(String msg) {
        
        Toast.makeText(ImportActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    
    
    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSerialPortTool.close();
    }




    private void DisplayError(String resourceId) {
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Aviso de Erro");
        b.setMessage(resourceId);
        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
                
//                ImportActivity.this.finish();
            }
        });
        b.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            sendData();
        } else if (v.getId() == R.id.btn_setting_sp) {
            Intent intent = new Intent(ImportActivity.this, SerialPortSettingActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        }
    }
    
    
    /**
     * 获取版本信息
     * @return
     */
    private String getAppInfo() {
        
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
            int versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return  versionName;
        } catch (Exception e) {
        }
        return null;
    }
}
