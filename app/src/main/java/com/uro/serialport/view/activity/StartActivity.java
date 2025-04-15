package com.uro.serialport.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.uro.serialport.MainActivity;
import com.urovo.serialport.BuildConfig;

/**
 * @author ZhengJuE
 * @brief description
 * @date 2023-01-09
 */
public class StartActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.VENDOR == 2){
            startActivity(new Intent(StartActivity.this,SerialPortCommunicationActivity.class));
        }else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
        StartActivity.this.finish();
    }
}
