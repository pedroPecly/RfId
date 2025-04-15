package com.uro.utils;

/**
 * @author 作者 :wrc
 * @version 创建时间：2019年11月5日 上午10:52:25 类说明
 */
public class ConfigurationUtils {

	public static int intSetType = 0;// 0 POS 互导    1：POS-PC 互导

	public static void setSerialPort(int type ,int index, String vString) {
		if (type == 0) {
			SharePreTool.setInt(SharePreKey.POS_SERIALPORT_INDEX, index);
			SharePreTool.setString(SharePreKey.POS_SERIALPORT_VALUE, vString);
		}else if (type == 1){
			SharePreTool.setInt(SharePreKey.PC_SERIALPORT_INDEX, index);
			SharePreTool.setString(SharePreKey.PC_SERIALPORT_VALUE, vString);
		}
	}

	public static void setBaudrates(int type ,int index, int value) {
		if (type == 0) {
			SharePreTool.setInt(SharePreKey.POS_BAUDRATES_INDEX, index);
			SharePreTool.setInt(SharePreKey.POS_BAUDRATES_VALUE, value);
		}else if (type == 1){
			SharePreTool.setInt(SharePreKey.PC_BAUDRATES_INDEX, index);
			SharePreTool.setInt(SharePreKey.PC_BAUDRATES_VALUE, value);
		}

	}

	public static int getSerialPort_Index(int type) {
		int index = 0;
		if (type == 0) {
			index = SharePreTool.getInt(SharePreKey.POS_SERIALPORT_INDEX, 0);
		}else if (type == 1){
			index =SharePreTool.getInt(SharePreKey.PC_SERIALPORT_INDEX, 0);
		}
		return index;
	}

	public static String getSerialPort_Value(int type) {
		String value = "";
		if (type == 0) {
			value = SharePreTool.getString(SharePreKey.POS_SERIALPORT_VALUE, "/dev/ttyHSL1");
		}else if (type == 1){
			value = SharePreTool.getString(SharePreKey.PC_SERIALPORT_VALUE, "/dev/ttyGS0");
		}
		return value;
	}

	public static int getBaudrates_Index(int type) {
		int index = 0;
		if (type == 0) {
			index =SharePreTool.getInt(SharePreKey.POS_BAUDRATES_INDEX, 12);
		}else if (type == 1){
			index = SharePreTool.getInt(SharePreKey.PC_BAUDRATES_INDEX, 12);
		}
		return index;
	}

	public static int getBaudrates_Value(int type) {
		int value = 9600;
		if (type == 0) {
			value =SharePreTool.getInt(SharePreKey.POS_BAUDRATES_VALUE, 9600);
		}else if (type == 1){
			value =SharePreTool.getInt(SharePreKey.PC_BAUDRATES_VALUE, 9600);
		}
		return value;
	}
}
