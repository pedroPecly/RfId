package com.uro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import android.util.Log;

import com.urovo.rfid.RfidReaderMange;
import com.urovo.utils.LogHelper;

/**
 * Receptor para tratamento de eventos USB relacionados ao RFID
 * @author ZhengJuE
 * @date 2023-01-11
 */
public class AppReceiver extends BroadcastReceiver {
    private static final String TAG = AppReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            LogHelper.e(TAG + ": onReceive: " + action);
            
            if (TextUtils.isEmpty(action)) {
                return;
            }
            
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                handleUsbDetached(intent);
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                handleUsbAttached(intent);
            } else if (UsbManager.ACTION_USB_STATE.equals(action)) {
                handleUsbState(intent);
            }
        } catch (Exception e) {
            LogHelper.e(TAG, "Erro no processamento de evento USB: " + e.getMessage());
        }
    }
    
    /**
     * Trata a desconexão de dispositivo USB
     */
    private void handleUsbDetached(Intent intent) {
        try {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null) {
                LogHelper.e(TAG, "Dispositivo USB desconectado: " + device.getDeviceName());
            }
            // Notify RfidReaderMange if needed
            RfidReaderMange.getInstance().onHandleUsbStateChange(false);
        } catch (Exception e) {
            LogHelper.e(TAG, "Erro ao processar desconexão USB: " + e.getMessage());
        }
    }
    /**
     * Trata a conexão de dispositivo USB
     */
    private void handleUsbAttached(Intent intent) {
        try {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null) {
                LogHelper.e(TAG, "Dispositivo USB conectado: " + device.getDeviceName());
            }
            // Notify RfidReaderMange
            RfidReaderMange.getInstance().onHandleUsbStateChange(true);
        } catch (Exception e) {
            LogHelper.e(TAG, "Erro ao processar conexão USB: " + e.getMessage());
        }
    }
    
    /**
     * Trata mudanças no estado geral do USB
     */
    private void handleUsbState(Intent intent) {
        try {
            boolean connected = intent.getExtras() != null && intent.getExtras().getBoolean("connected", false);
            LogHelper.e(TAG, "Estado USB: connected=" + connected);
            RfidReaderMange.getInstance().onHandleUsbStateChange(connected);
        } catch (Exception e) {
            LogHelper.e(TAG, "Erro ao processar estado USB: " + e.getMessage());
        }
    }
}
