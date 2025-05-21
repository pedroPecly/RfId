package com.uro.serialport.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.uro.serialport.SerialPortSettingActivity;
import com.uro.utils.ByteUtil;
import com.uro.utils.ConfigurationUtils;
import com.uro.utils.tlv.TLV;
import com.uro.utils.tlv.TLVTools;
import com.urovo.serial.utils.SerialPortListener;
import com.urovo.serial.utils.SerialPortTool;
import com.urovo.serialport.R;
import com.urovo.serialport.databinding.ActivitySerialPortCommunicationBinding;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZhengJuE
 * @brief description
 * @date 2023-01-09
 */
public class SerialPortCommunicationActivity extends BaseActivity{
    private final String TAG = SerialPortCommunicationActivity.class.getSimpleName();
    private ActivitySerialPortCommunicationBinding mBinding;
    private boolean isOpenSerialPort = false;
    private String sendData = "02699601CEE802001501";
    private String defaultNodeName = "/sys/devices/virtual/pogo/pogo_pin/pogo_otg_mode"; //host pogoping
    private SerialPortTool mSerialPortTool;
    private StringBuffer receiveSBuffer = new StringBuffer();
    private byte[] totalResponseDataBuff = new byte[1024];
    private byte[] responseDataBuff = new byte[0];
    private int responseDataLen = 0;
    private int currentResponseLen = 0;
    private TLVTools mTLVTools = new TLVTools();
    private SerialPortListener mSerialPortListener = new SerialPortListener() {
        @Override
        public void onReceive(byte[] data) {
//            String hexStr = ByteUtil.bytes2HexString(data);
//            byte[] byteData = hexStr.getBytes();
//            String result = null;
//            try {
//                result = new String(byteData,"UTF-8");
//                //result = ByteUtil.bytes2HexString(byteData);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            receiveSBuffer.append(result + "");
//            if (receiveSBuffer.length() > 0 && (receiveSBuffer.toString().toUpperCase().endsWith("D20400000000") || receiveSBuffer.toString().toUpperCase().endsWith("D20401000000"))){
//                outputNotWrapText(result+"\n");
//                onReceiveSerialData(receiveSBuffer.toString());
//                receiveSBuffer.setLength(0);
//            }else {
//                outputNotWrapText(result);
//            }


            try {
                Log.e(TAG,"receive start: " + ByteUtil.bytes2HexString(data));
                System.arraycopy(data, 0, totalResponseDataBuff, currentResponseLen, data.length);
                currentResponseLen += data.length;
                Log.e(TAG,"currentResponseLen:"+currentResponseLen);                //Sem obter posição de comprimento
                if (currentResponseLen < 8) {
                    return;
                }
                //Ler posição de comprimento
                byte[] dataLenBuff = new byte[2];
                if (currentResponseLen >= 8) {
                    System.arraycopy(totalResponseDataBuff, 6, dataLenBuff, 0, 2);
                    Log.e(TAG , "dataLenBuff: " + ByteUtil.bytes2HexString(dataLenBuff));
                    responseDataLen = ByteUtil.bigEndianConvertToInt(dataLenBuff, 2);
                    Log.e(TAG , "responseDataLen: " + responseDataLen);
                }
                Log.e(TAG , "currentResponseLen  Start: " + currentResponseLen);
                if (currentResponseLen < responseDataLen + 8) {
                    // mHandler.sendEmptyMessageDelayed(MESSAGE_DELAY, TIME_OUT);
                    return;
                }
                responseDataBuff = new byte[responseDataLen];
                System.arraycopy(totalResponseDataBuff, 8, responseDataBuff, 0, responseDataBuff.length);
                Log.e(TAG , "responseDataBuff: " + ByteUtil.bytes2HexString(responseDataBuff));

                byte[] STXBuff = new byte[4];
                System.arraycopy(totalResponseDataBuff, 0, STXBuff, 0, 4);
                Log.e(TAG , "STXBuff: " + ByteUtil.bytes2HexString(STXBuff));
                byte[] CRCBuff = new byte[2];
                System.arraycopy(totalResponseDataBuff, 4, CRCBuff, 0, 2);
                Log.e(TAG , "CRCBuff: " + ByteUtil.bytes2HexString(CRCBuff));                //Verificação CRC
                byte[] CRCResouceBuff = new byte[dataLenBuff.length + responseDataBuff.length];
                System.arraycopy(dataLenBuff, 0, CRCResouceBuff, 0, dataLenBuff.length);
                System.arraycopy(responseDataBuff, 0, CRCResouceBuff, dataLenBuff.length, responseDataBuff.length);
                Log.e(TAG,"CRCResouceBuff:"+ByteUtil.bytes2HexString(CRCResouceBuff));
                byte[] CRCBuff2 = ByteUtil.hexString2Bytes(ByteUtil.CrcCall(CRCResouceBuff));
                if (ByteUtil.comparabytes(CRCBuff, CRCBuff2, CRCBuff.length) != 0) {                    Arrays.fill(totalResponseDataBuff, (byte) 0x00);
                    currentResponseLen = 0;
                    responseDataBuff = new byte[0];
                    Log.e(TAG,"Falha na verificação CRC");
                    //sendHandlerMessage(MESSAGE_FAILED, "CRC校验失败", 0);
                    return;
                }                byte[] moduleCodeBuff = new byte[1]; //código do módulo
                byte[] functionCodeBuff = new byte[1]; //código de função
                byte[] commandStatusBuff = new byte[2]; //código de status
                byte[] dataBuff = new byte[responseDataLen - 4];
                System.arraycopy(responseDataBuff, 0, moduleCodeBuff, 0, 1);
                System.arraycopy(responseDataBuff, 1, functionCodeBuff, 0, 1);
                System.arraycopy(responseDataBuff, 2, commandStatusBuff, 0, 2);
                System.arraycopy(responseDataBuff, 4, dataBuff, 0, dataBuff.length);

                Arrays.fill(totalResponseDataBuff, (byte) 0x00);
                currentResponseLen = 0;
                responseDataBuff = new byte[0];

                if (!(commandStatusBuff[0] == 0x00
                        && commandStatusBuff[1] == 0x00)) {                    outputColorText(TextColor.RED,"Falha na resposta dos dados");
                    //String errMessage = "获取当前SE固件版本信息失败";
                    ///sendHandlerMessage(MESSAGE_FAILED, errMessage + "：" + BytesUtil.bytes2HexString(commandStatusBuff), 0);
                    return;
                }                //Obter a versão atual de firmware
                if (moduleCodeBuff[0] == (byte) 0x15 && functionCodeBuff[0] == 0x01) {String respData = ByteUtil.bytes2HexString(dataBuff);
                    Log.e(TAG,"Dados da resposta:"+respData);
                    outputText("Dados da resposta:"+respData);
                    Log.e(TAG,"validData:"+respData);
                    List<TLV> tlvList = mTLVTools.unpack(respData);
                    if (tlvList != null && tlvList.size() > 0){
                        outputText("TLV:"+ new Gson().toJson(tlvList));
                        String tlvD0 = mTLVTools.TLV_Find("D0",respData);
                        String tlvD2 = mTLVTools.TLV_Find("D2",respData);
                        outputText("DO:"+tlvD0 +"\nD2:"+tlvD2);
                        Log.e(TAG,"DO:"+tlvD0 +"\nD2:"+tlvD2);
                        if (!TextUtils.isEmpty(tlvD0)){
                            byte[] tlvD0Buffer = ByteUtil.hexString2Bytes(tlvD0);
                            int tlvD0Int = ByteUtil.bigEndianConvertToInt(tlvD0Buffer,tlvD0Buffer.length);
                            Log.e(TAG,"tlvD0Int:"+tlvD0Int);
                            outputText("tlvD0Int:"+tlvD0Int);                            int percent = convertPower(tlvD0Int);
                            Log.e(TAG,"Bateria:"+percent);
                            outputColorText(TextColor.RED,"Bateria:"+percent);
                        }
                        if (!TextUtils.isEmpty(tlvD2)){
                            //int tlvD2Int = Integer.parseInt(tlvD2,16);
                            byte[] tlvD2Buffer = ByteUtil.hexString2Bytes(tlvD2);
                            int tlvD2Int = ByteUtil.bigEndianConvertToInt(tlvD2Buffer,tlvD2Buffer.length);
                            Log.e(TAG,"tlvD2Int:"+tlvD2Int);
                            outputText("tlvD2Int:"+tlvD2Int);                            String isCharging = tlvD2Int == 1? "carregando":"descarregando";
                            outputColorText(TextColor.RED,isCharging);
                        }

                    }else {
                        outputColorText(TextColor.RED,"Erro de formato de dados:"+respData);
                    }
                    return;
                }
                //sendHandlerMessage(MESSAGE_FAILED, "固件版本获取失败，未知功能码：" + BytesUtil.bytes2HexString(functionCodeBuff), 0);
            } catch (Exception e) {
                e.printStackTrace();                outputColorText(TextColor.RED,"Erro de formato de dados:"+e.toString());
                //sendHandlerMessage(MESSAGE_FAILED, "固件版本获取失败：" + e.getMessage(), 0);
            }
        }

        @Override
        public void onFail(String code, String msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_serial_port_communication);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        mBinding.etInputData.setText(sendData);
        mBinding.etInputData.setSelection(mBinding.etInputData.length());
        mBinding.btnSettingSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SerialPortCommunicationActivity.this, SerialPortSettingActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                sendData = mBinding.etInputData.getText().toString();
                if (TextUtils.isEmpty(sendData)){
                    showToast("Por favor, insira os dados para enviar");
                    return;
                }
                sendDataBySerialPort(sendData);
            }
        });

        mBinding.btnOpenHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOTGEnable(true,defaultNodeName,"2");
            }
        });

        mBinding.btnCloseHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOTGEnable(false,defaultNodeName,"0");
            }
        });

        mBinding.btnOpenSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSerialPort();
            }
        });

        mBinding.btnCloseSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSerialPortTool != null){
                    mSerialPortTool.close();                }
                mBinding.tvStatus.setText("Porta serial fechada");
            }
        });
    }

    private void sendDataBySerialPort(String data){
        try {
            byte[] sendDataBuffer = ByteUtil.hexString2Bytes(data);
            receiveSBuffer.setLength(0);
            String result = mSerialPortTool.sendData(sendDataBuffer, sendDataBuffer.length);            if (result != null){
                isOpenSerialPort = false;
                outputColorText(TextColor.RED,"Falha no envio: "+result);
            }else {
                outputText(data+"\nEnviado com sucesso");
            }
        }catch (Exception e){
            e.printStackTrace();            isOpenSerialPort = false;
            Log.e(TAG,"sendDataBySerialPort EE:"+e.toString());
            outputColorText(TextColor.RED,"Falha no envio: "+e.toString());
        }
    }

    private void onReceiveSerialData(String receiveData){
        Log.e(TAG,"receiveData:"+receiveData);
        outputText("onReceiveSerialData");
        if (!TextUtils.isEmpty(receiveData) && receiveData.toUpperCase().startsWith("02699601867B160015010000")){
            String validData = receiveData.substring(receiveData.indexOf("02699601867B160015010000"));
            Log.e(TAG,"validData:"+validData);
            List<TLV> tlvList = new TLVTools().unpack(validData);            if (tlvList != null && tlvList.size() > 0){
                outputText("TLV:"+ new Gson().toJson(tlvList));
            }else {
                outputColorText(TextColor.RED,"Erro de formato de dados: "+validData);
            }

        }
    }

    private void openSerialPort(){
        String serialStr = ConfigurationUtils.getSerialPort_Value(0);
        int baudrates = ConfigurationUtils.getBaudrates_Value(0);
        Log.e(TAG,"pathName:"+serialStr+">>speed:"+baudrates);
        baudrates = 115200;
        mSerialPortTool = new SerialPortTool("",baudrates);
        String result = mSerialPortTool.open();
        Log.e(TAG,"Open  SerialPort:"+result);        if (result != null){
            isOpenSerialPort = false;
            showDialogError(result);
            mBinding.tvStatus.setText("Porta serial não aberta");
        }else {
            isOpenSerialPort = true;
            mSerialPortTool.setOnListener(mSerialPortListener);
            mBinding.tvStatus.setText("Aberta: "+mSerialPortTool.getPathName()+"  "+mSerialPortTool.getCurrentSpeed());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openSerialPort();
    }

    private  boolean setOTGEnable(boolean otgEnable,String POGO_NODE_YOUKAI,String nodeVal){
//        try {
//            DeviceManager mDeviceManager = new DeviceManager();
//            //mDeviceManager.setSettingProperty("File-/sys/devices/virtual/Usb_switch/usbswitch/function_otg_en", otgEnable ? Integer.toString(2) : Integer.toString(0));
//            boolean iRet = mDeviceManager.setSettingProperty("File-"+POGO_NODE_YOUKAI,nodeVal);
//            mDeviceManager.setSettingProperty("System-sys.hostkey.switch", otgEnable ? "1" : "0");
//            Log.e(TAG,POGO_NODE_YOUKAI+"\notgEnable："+otgEnable+"\niRet:"+iRet);
//            String ret = mDeviceManager.getSettingProperty("File-"+POGO_NODE_YOUKAI);
//            Log.e(TAG,"RET:"+ret);
//            outputText(""+ret);
//            return iRet;
//            //Toast.makeText(getApplicationContext(), mDeviceManager.getSettingProperty("File-/sys/devices/virtual/Usb_switch/usbswitch/function_otg_en"), Toast.LENGTH_LONG).show();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return false;
    }

    public static int convertPower(int vol){
        int[] batteryLevelList = {4180,4170,4160,4150,4145,4140,4134,4124,4114,4104,
                4094,4085,4075,4065,4046,4036,4027,4018,4009,3999,3990,3981,3973,3955,
                3946,3937,3929,3920,3911,3903,3894,3876,3867,3858,3849,3840,3831,3821,
                3811,3793,3784,3775,3767,3759,3751,3743,3736,3728,3713,3706,3698,3692,
                3685,3680,3675,3670,3661,3657,3654,3650,3647,3644,3641,3638,3632,3630,
                3627,3624,3621,3619,3616,3613,3607,3605,3602,3598,3595,3592,3589,3585,
                3577,3572,3567,3562,3556,3549,3543,3537,3529,3513,3504,3498,3492,3486,
                3479,3473,3468,3457,3451,3445};
        //037e转10进制894
        int  battery_vol = (int) (vol * 4.28);
        int SYS_VOLTAGE_MAX = 4191;
        int SYS_VOLTAGE_MIN = 3405;
        int percent = 0;
        if (battery_vol >= SYS_VOLTAGE_MAX || battery_vol >= batteryLevelList[0]){
            percent=100;
        }else if (battery_vol <= SYS_VOLTAGE_MIN ){
            percent=0;
        }else {
            int bat = 0;
            for (bat = 0; bat < 100; bat++) {
                if(battery_vol < batteryLevelList[bat] && battery_vol >= batteryLevelList[bat+1])
                    break;
            }
            percent = 99-bat;
        }
        return percent;
    }

}
