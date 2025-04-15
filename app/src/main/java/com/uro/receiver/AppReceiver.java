package com.uro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.urovo.rfid.RfidReaderMange;
import com.urovo.utils.LogHelper;

/**
 * @author ZhengJuE
 * @brief description
 * @date 2023-01-11
 */
public class AppReceiver extends BroadcastReceiver {
    private final String TAG = AppReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        LogHelper.e(TAG+"：onReceive:" + intent.getAction());
        if (TextUtils.equals(intent.getAction(),"android.hardware.usb.action.USB_DEVICE_DETACHED")){
            //USB unplug
        }else if (TextUtils.equals(intent.getAction(),"android.hardware.usb.action.USB_DEVICE_ATTACHED")){
            //USB insertion
            //RfidReaderMange.getInstance().onHandleUsbStateChange(true);
        }

//        else if (TextUtils.equals(intent.getAction(),"android.hardware.usb.action.USB_STATE")){
//            boolean connected = intent.getExtras().getBoolean("connected");
//            LogHelper.e(TAG,"connected:"+connected);
//            RfidReaderMange.getInstance().onHandleUsbStateChange(connected);
//        }
    }

}
