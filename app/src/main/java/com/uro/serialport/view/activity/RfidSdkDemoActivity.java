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
 * @author ZhengJuE
 * @brief description
 * @date 2023-01-11
 */
public class RfidSdkDemoActivity extends BaseActivity{
    private ActivityRfidSdkDemoBinding mBinding;
    private RfidReaderMange mRfidReaderMange;
    private RfidReaderMangeModuleInfoCallBack mangeModuleInfoCallBack = new RfidReaderMangeModuleInfoCallBack() {
        @Override
        public void onReadBatteryInfo(Map<String, Object> respMap) {
            int batteryStatus = (int) respMap.get(GlobalConstant.RespDataKey.BatteryStatus);
            outputColorText(TextColor.GREEN, DateUtil.getDateTime1(new Date())+">>"+GlobalConstant.RespDataKey.BatteryLevel+": "+respMap.get(GlobalConstant.RespDataKey.BatteryLevel)+"%");
            outputColorText(TextColor.GREEN,DateUtil.getDateTime1(new Date())+">>"+GlobalConstant.RespDataKey.BatteryStatus+": "+batteryStatus+"（"+(batteryStatus==1?"charging":"not charging")+")");
            mBinding.tvStatus.setText(mRfidReaderMange.getCurrentPathName() + " "+ mRfidReaderMange.getCurrentSpeed());
        }

        @Override
        public void onModuleStatus(String status) {
            mBinding.tvModuleStatus.setText("ModuleStatus:"+status);
        }

        @Override
        public void onFail(String errorCode, String msg){
            outputColorText(TextColor.RED, DateUtil.getDateTime1(new Date())+">>"+"Active:"+errorCode+":"+msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rfid_sdk_demo);
        mRfidReaderMange = RfidReaderMange.getInstance();
        int iRet = mRfidReaderMange.initialize(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        mBinding.btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iRet = mRfidReaderMange.initialize(RfidSdkDemoActivity.this);
                outputText(getString(R.string.initialize) +":"+iRet+" "+mRfidReaderMange.getErrorMessage(iRet));
                if (iRet == GlobalConstant.SerialPortResp.Success)
                    mBinding.tvStatus.setText(mRfidReaderMange.getCurrentPathName() + " "+ mRfidReaderMange.getCurrentSpeed());
            }
        });
        mBinding.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputText(""+getString(R.string.active_read_battery_info));
                try {
                    mRfidReaderMange.startMonitorModuleInfo(5,mangeModuleInfoCallBack);
                } catch (Exception e) {
                    e.printStackTrace();
                    outputColorText(TextColor.RED,getString(R.string.active_read_battery_info)+" Exception:"+e.toString());
                }
            }
        });

        mBinding.btnReaderModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputText(getString(R.string.rfid_reader_model)+":"+mRfidReaderMange.getRfidModel());
            }
        });

        mBinding.btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRfidReaderMange.release();
                outputText("release success");
                mBinding.tvStatus.setText(getString(R.string.disconnect));
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mRfidReaderMange != null){
            mRfidReaderMange.release();
        }
        super.onDestroy();
    }
}
