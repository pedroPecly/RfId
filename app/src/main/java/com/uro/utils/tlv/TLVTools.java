package com.uro.utils.tlv;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Create by xxf 202012
 *
 * */
public class TLVTools {

    public List<TLV> tlvList = new ArrayList<>();

    public List<TLV> unpack(String tlv){
        int current = 0;//遍历数据标签

        int i,j,Ttaglen=0,Htaglen=0;

        tlvList.clear();

        //将string数据转换成byte
        byte[] data =hexStrToBytes(tlv.toUpperCase());
        String SRC=tlv.toUpperCase();
        while(current < data.length)
        {
            TLV tlvData = new TLV();

            Htaglen=0;
            if ((data[current] & (byte)0x1F) == 0x1F)
            {

                current++;
                Htaglen++;
                do
                {
                    Htaglen++;
                }
                while((data[current++] & (byte)0x80)!=0);
            }
            else
            {
                current++;
                Htaglen++;
            }


            tlvData.setTag(SRC.substring(current*2-Htaglen*2,current*2));

            if ((data[current] & 0x80)!=0) //len
            {
                i = (data[current++] & 0x7F);
                for (j = 0, Ttaglen = 0; j < i; j++)
                {
                    Ttaglen <<= 8;
                    Ttaglen +=data[current++];
                }

            }
            else
            {
                Ttaglen =data[current++];
            }
            tlvData.setLen(Ttaglen);

            byte[] temp = new byte[Ttaglen];
            System.arraycopy(data, current, temp, 0, Ttaglen);
            tlvData.setValue(bytesToHex(temp));
            current += Ttaglen;

            tlvList.add(tlvData);
        }

        return tlvList;
    }

    //return tag`s value
    public String TLV_Find(String tag,String TLV)
    {
        int i,j,Ttaglen=0,Htaglen=0;
        byte[] data =hexStrToBytes(TLV.toUpperCase());
        String SRC=TLV.toUpperCase();
        int current = 0;//遍历数据标签
        while(current < data.length)
        {
            Htaglen=0;
            if ((data[current] & (byte)0x1F) == 0x1F)
            {

                current++;
                Htaglen++;
                do
                {
                    Htaglen++;
                }
                while((data[current++] & (byte)0x80)!=0);
            }
            else
            {
                current++;
                Htaglen++;
            }
            String tTAG=SRC.substring(current*2-Htaglen*2,current*2);
            if ((data[current] & 0x80)!=0) //len
            {
                i = (data[current++] & 0x7F);
                for (j = 0, Ttaglen = 0; j < i; j++)
                {
                    Ttaglen <<= 8;
                    Ttaglen +=data[current++]&0xFF;
                }

            }
            else
            {
                Ttaglen =data[current++]&0xFF;
            }

            if(tag.equals(tTAG))
            {
                byte[] temp = new byte[Ttaglen];
                System.arraycopy(data, current, temp, 0, Ttaglen);
                return bytesToHex(temp);
            }

            current += Ttaglen;
        }
        return "";
    }


    public static HashMap<String,String> decodeTLV(String tlv)
    {
        int current = 0;//遍历数据标签
        HashMap<String,String> hashtable=new HashMap<>();
        int i,j,Ttaglen=0,Htaglen=0;

        hashtable.clear();

        //将string数据转换成byte
        byte[] data = StrToHexByte(tlv.toUpperCase());
        String SRC=tlv.toUpperCase();
        while(current < data.length)
        {
            Htaglen=0;
            if ((data[current] & (byte)0x1F) == 0x1F)
            {

                current++;
                Htaglen++;
                do
                {
                    Htaglen++;
                }
                while((data[current++] & (byte)0x80)!=0);
            }
            else
            {
                current++;
                Htaglen++;
            }

            String TAG=SRC.substring(current*2-Htaglen*2,current*2);


            if ((data[current] & 0x80)!=0) //len
            {
                i = (data[current++] & 0x7F);
                for (j = 0, Ttaglen = 0; j < i; j++)
                {
                    Ttaglen <<= 8;
                    Ttaglen +=data[current++]&0xFF;
                }

            }
            else
            {
                Ttaglen =data[current++]&0xFF;
            }


            byte[] temp = new byte[Ttaglen];
            System.arraycopy(data, current, temp, 0, Ttaglen);
            String VALUE= bytesToHexString(temp);

            current += Ttaglen;

            hashtable.put(TAG, VALUE);
        }

        return hashtable;
    }



    public static String New_TLV(String strTag,String strVal)
    {
        String strTlv ="";
        String lenStr ="";
        int strValLen=strVal.length();
        strTlv += strTag;
        if(!strVal.isEmpty())
        {
            if(strValLen/2<=127)
            {
                lenStr =Integer.toHexString(strVal.length()/2).toUpperCase();
                if(lenStr.length()<2)
                {
                    strTlv += "0";
                    strTlv += lenStr;
                }
                else
                {
                    strTlv += lenStr;
                }
            }
            else if(strValLen/2<=255)
            {
                lenStr =Integer.toHexString(strVal.length()/2);
                if(lenStr.length()<2)
                {
                    strTlv += "810";
                    strTlv += lenStr;
                }
                else
                {
                    strTlv += "81";
                    strTlv += lenStr;
                }
            }
            else
            {
                lenStr =Integer.toHexString(strVal.length()/2);
                if(lenStr.length()<4)
                {
                    strTlv += "820";
                    strTlv += lenStr;
                }
                else
                {
                    strTlv += "82";
                    strTlv += lenStr;
                }
            }

            strTlv += strVal;
        }
        else
        {
            strTlv += "00";
        }
        return strTlv.toUpperCase();
    }




    public static String Remove_TLV(String strTag,String TLV)
    {
        int i,j,Ttaglen=0,Htaglen=0,TotalLen=0;
        if(TextUtils.isEmpty(TLV))
            return "";
        byte[] data =StrToHexByte(TLV.toUpperCase());
        TotalLen=data.length;
        String SRC=TLV.toUpperCase();
        int current = 0;//遍历数据标签
        int TagCurrent=0;
        while(current < data.length)
        {
            Htaglen=0;
            if ((data[current] & (byte)0x1F) == 0x1F)
            {

                current++;
                Htaglen++;
                do
                {
                    Htaglen++;
                }
                while((data[current++] & (byte)0x80)!=0);
            }
            else
            {
                current++;
                Htaglen++;
            }
            TagCurrent=current-Htaglen;
            String tTAG=SRC.substring(current*2-Htaglen*2,current*2);
            if ((data[current] & 0x80)!=0) //len
            {
                i = (data[current++] & 0x7F);
                for (j = 0, Ttaglen = 0; j < i; j++)
                {
                    Ttaglen <<= 8;
                    Ttaglen +=data[current++]&0xFF;
                }

            }
            else
            {
                Ttaglen =data[current++]&0xFF;
            }
            // LOG.d("applog", "current:"+current +" Ttaglen:"+Ttaglen);

            if(strTag.equals(tTAG))
            {
                byte[] tempRight = new byte[TotalLen-(current+Ttaglen)];
                System.arraycopy(data, current+Ttaglen, tempRight, 0, TotalLen-(current+Ttaglen));
                byte[] tempLift =new byte[TagCurrent];
                System.arraycopy(data,0,tempLift, 0, TagCurrent);
                byte[] temp=byteMerger(tempLift,tempRight);
                return bytesToHexString(temp);
            }

            current += Ttaglen;
        }
        return TLV;

    }



    //不带头结点的TLV，增加子结点
    public static String AddChild_TLV(String RootTLV,String strTag,String strVal)
    {
        if(TextUtils.isEmpty(RootTLV)||TextUtils.isEmpty(strTag)||TextUtils.isEmpty(strVal))
            return "";
        return RootTLV+New_TLV(strTag, strVal);
    }



    public static String New_TLV(int tag,byte[] value,int valuelen)
    {
        byte[] new_tlvbuffer=new byte[1024];
        int index=0;
        if(((tag>>24)&0xFF)!=0)
        {
            new_tlvbuffer[index++] =(byte) ((tag>>24)&0xFF);
            new_tlvbuffer[index++] =(byte) ((tag>>16)&0xFF);
            new_tlvbuffer[index++] =(byte) ((tag>>8)&0xFF);
            new_tlvbuffer[index++] = (byte) (tag&0xFF);
        }
        else if(((tag>>16)&0xFF)!=0)
        {
            new_tlvbuffer[index++] = (byte) ((tag>>16)&0xFF);
            new_tlvbuffer[index++] = (byte) ((tag>>8)&0xFF);
            new_tlvbuffer[index++] = (byte) (tag&0xFF);
        }
        else if(((tag>>8)&0xFF)!=0)
        {
            new_tlvbuffer[index++] = (byte) ((tag>>8)&0xFF);
            new_tlvbuffer[index++] = (byte) (tag&0xFF);
        }
        else if((tag&0xFF)!=0)
        {
            new_tlvbuffer[index++] = (byte) (tag&0xFF);
        }
        if(valuelen>=256)
        {

            new_tlvbuffer[index++] = (byte)0x82;
            new_tlvbuffer[index++] = (byte)(valuelen/256);
            new_tlvbuffer[index++] = (byte)(valuelen%256);
        }
        else if(valuelen>=128&&valuelen<256)
        {

            new_tlvbuffer[index++] =(byte) 0x81;
            new_tlvbuffer[index++] = (byte)valuelen;
        }
        else
        {

            new_tlvbuffer[index++] = (byte)valuelen;
        }

        System.arraycopy(value, 0, new_tlvbuffer,index,valuelen);
        byte[] temp=new byte[index+valuelen];
        System.arraycopy(new_tlvbuffer, 0, temp,0,index+valuelen);

        return bytesToHex(temp);
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    private static byte[] StrToHexByte(String str) {
        if(str == null) {
            return null;
        } else if(str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];

            for(int i = 0; i < len; ++i) {
                buffer[i] = (byte)Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }

            return buffer;
        }
    }

    private static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for(int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if(sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    //hex数据转换成byte
    public byte[] hexStrToBytes(String data)    {
        if (data == null || "".equals(data)) {
            return null;
        }
        int len = data.length() / 2;
        byte[] result = new byte[len];

        //将每个char字符单独转换成byte数据
        char[] chArr = data.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
        }
        return result;
    }


    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    private static char[] HEX_VOCABLE = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    //bytes数组转换成string
    public static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            //获取高4位的数据值，正好对应HEX_VOCABLE数组中的下标，和相应char的值
            int high = (b >> 4) & 0x0f;
            //获取低4位的数据值，正好对应HEX_VOCABLE数组中的下标，和相应char的值
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }

}



