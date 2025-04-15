package com.uro.serialport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uro.utils.ByteUtil;
import com.uro.utils.ConfigurationUtils;
import com.urovo.serial.utils.SerialPortListener;
import com.urovo.serial.utils.SerialPortTool;
import com.urovo.serialport.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.UnsupportedEncodingException;

public class DowloadPCActivity extends AppCompatActivity implements View.OnClickListener {
    
    private TextView mTvSend;//发送的数据
    private TextView mTvReceive;//接收的实际是数据
    private TextView mTvResult;//处理后的数据
    private EditText mEdSend;//输入框
    private Button mBtnSend,settingSerPort;
    private SerialPortTool mSerialPortTool;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        setTitle(getResources().getString(R.string.app_name)+" v"+getAppInfo()+"       PC导入");
    
        mTvSend = findViewById(R.id.tv_send);
        mTvReceive = findViewById(R.id.tv_receive);
        mTvResult = findViewById(R.id.tv_result);
        mEdSend = findViewById(R.id.ed_send);
        mBtnSend = findViewById(R.id.button);
        mBtnSend.setOnClickListener(this);
        settingSerPort =  findViewById(R.id.btn_setting_sp);
        settingSerPort.setOnClickListener(this);

        String serialStr = ConfigurationUtils.getSerialPort_Value(1);
        int baudrates = ConfigurationUtils.getBaudrates_Value(1);
        mSerialPortTool = new SerialPortTool( serialStr,baudrates);
        initViewData();
    }
    
    
    private void initViewData() {
        
        mEdSend.setText("0123456789ABCDEF");
    }
    
    
    private String getSendTextView() {
        
        String str =  mEdSend.getText().toString().trim();
        return   str ;
    }
    
    
    private void sendData() {
    
        try {
            String sendStr = getSendTextView();
            mTvSend.setText("发送数据：" + sendStr);
//            byte[] sendData = sendStr.getBytes("UTF-8");
            byte[] sendData = ByteUtil.hexString2Bytes(sendStr);
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
    
            String result = null;
            try {
                result = new String(data,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            
            receiveSBuffer.append(result + "  ");
            mTvReceive.setText("实际接收：\n" + receiveSBuffer.toString());
    
            resultsBuffer.append(result);
            if (resultsBuffer.toString().indexOf(".") > 0) {
                String sub = resultsBuffer.substring(resultsBuffer.indexOf(",") + 1, resultsBuffer.indexOf("."));
                mTvResult.setText("接收的有效数据：\n" + sub);
                clearData();
            }
            
        }

        @Override
        public void onFail(String code, String msg) {

        }
    };
    
    
    /**
     * 清空 StringBuffer 数据
     */
    private void clearData() {

//        receiveSBuffer.setLength(0);
        resultsBuffer.setLength(0);
    }
    
    
    @Override
    protected void onResume() {
        
        super.onResume();
        String result = mSerialPortTool.open();
        if (result != null) {
            DisplayError(result);
            return;
        }
        mSerialPortTool.setOnListener(listener);
    }
    
    
    private void toast(String msg) {
        
        Toast.makeText(DowloadPCActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    
    
    @Override
    protected void onDestroy() {
        
        super.onDestroy();
        mSerialPortTool.close();
    }
    
    
    private void DisplayError(String resourceId) {
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("错误提示");
        b.setMessage(resourceId);
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
                
//                DowloadPCActivity.this.finish();
            }
        });
        b.show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button) {
            sendData();
        } else if (id == R.id.btn_setting_sp) {
            Intent intent = new Intent(DowloadPCActivity.this, SerialPortSettingActivity.class);
            intent.putExtra("type", 1);
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
