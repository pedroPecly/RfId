package com.uro.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class ByteUtil {

	private static final String TAG = ByteUtil.class.getSimpleName();
	
	private ByteUtil() {}

	public static byte hex2byte(char hex) {
		if (hex <= 'f' && hex >= 'a') {
			return (byte) (hex - 'a' + 10);
		}

		if (hex <= 'F' && hex >= 'A') {
			return (byte) (hex - 'A' + 10);
		}

		if (hex <= '9' && hex >= '0') {
			return (byte) (hex - '0');
		}

		return 0;
	}

	public static int bytes2Int(byte[] data) {
		if (data == null || data.length == 0) {
			return 0;
		}

		int total = 0;
		for (int i = 0; i < data.length; i++) {
			total += (data[i] & 0xff) << (data.length - i - 1) * 8;
		}
		return total;
	}

	public static String bytes2HexString(byte[] data) {
		if (data == null)
			return "";
		StringBuilder buffer = new StringBuilder();
		for (byte b : data) {
			String hex = Integer.toHexString(b & 0xff);
			if (hex.length() == 1) {
				buffer.append('0');
			}
			buffer.append(hex);
		}
		return buffer.toString().toUpperCase(Locale.US);
	}

	public static byte[] hexString2Bytes(String data) {
		if (data == null)
			return null;
		byte[] result = new byte[(data.length() + 1) / 2];
		if ((data.length() & 1) == 1) {
			data += "0";
		}
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (hex2byte(data.charAt(i * 2 + 1)) | (hex2byte(data
					.charAt(i * 2)) << 4));
		}
		return result;
	}

	public static String bcd2Ascii(final byte[] bcd) {
		if (bcd == null)
			return "";
		StringBuilder sb = new StringBuilder(bcd.length << 1);
		for (byte ch : bcd) {
			byte half = (byte) (ch >> 4);
			sb.append((char) (half + ((half > 9) ? ('A' - 10) : '0')));
			half = (byte) (ch & 0x0f);
			sb.append((char) (half + ((half > 9) ? ('A' - 10) : '0')));
		}
		return sb.toString();
	}

	public static byte[] ascii2Bcd(String ascii) {
		if (ascii == null)
			return null;
		if ((ascii.length() & 0x01) == 1)
			ascii = "0" + ascii;
		byte[] asc = ascii.getBytes();
		byte[] bcd = new byte[ascii.length() >> 1];
		for (int i = 0; i < bcd.length; i++) {
			bcd[i] = (byte) (hex2byte((char) asc[2 * i]) << 4 | hex2byte((char) asc[2 * i + 1]));
		}
		return bcd;
	}

	public static byte[] toBytes(String data, String charsetName) {
		try {
			return data.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] toBytes(String data) {
		return toBytes(data, "ISO-8859-1");
	}

	public static byte[] toGBK(String data) {
		return toBytes(data, "GBK");
	}

	public static byte[] toGB2312(String data) {
		return toBytes(data, "GB2312");
	}

	public static byte[] toUtf8(String data) {
		return toBytes(data, "UTF-8");
	}

	public static String fromBytes(byte[] data, String charsetName) {
		try {
			return new String(data, charsetName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String fromBytes(byte[] data) {
		return fromBytes(data, "ISO-8859-1");
	}

	public static String fromGBK(byte[] data) {
		return fromBytes(data, "GBK");
	}

	public static String fromGB2312(byte[] data) {
		return fromBytes(data, "GB2312");
	}

	public static String fromGB2312New(String data) {
		return fromGB2312(toBytes(data.trim()));
	}

	public static String fromUtf8(byte[] data) {
		return fromBytes(data, "UTF-8");
	}

	public static void dumpHex2(String msg, byte[] bytes) {
		int length = bytes.length;
		msg = (msg == null) ? "" : msg;
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("\n---------------------- " + msg
				+ "(len:%d) ----------------------\n", length));
		for (int i = 0; i < bytes.length; i++) {
			if (i % 16 == 0) {
				if (i != 0) {
					sb.append('\n');
				}
				sb.append(String.format("0x%08X    ", i));
			}
			sb.append(String.format("%02X ", bytes[i]));
		}
		sb.append("\n----------------------------------------------------------------------\n");
		Log.d(TAG, sb.toString());
	}

	public static String str2HexStr(String str) {
		final char[] mChars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		byte[] bs = str.getBytes();

		for (int i = 0; i < bs.length; i++) {
			sb.append(mChars[(bs[i] & 0xFF) >> 4]);
			sb.append(mChars[bs[i] & 0x0F]);
		}
		return sb.toString().trim();
	}

	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	  /**
     * ����������ʽ��ÿ�������ַ�ת��Ϊ"**"
     * ƥ�������ַ���������ʽ�� [\u4e00-\u9fa5]
     * ƥ��˫�ֽ��ַ�(������������)��[^\x00-\xff]
     * @param validateStr
     * @return
     */
    public static int getRegExpLength(String validateStr){
    	String temp = validateStr.replaceAll("[^\\x00-\\xff]", "**");
    	return temp.length();
    }

	/**
	 * 数组转换Int值，高低位转换
	 *
	 * @param data
	 * @param byteLen
	 * @return
	 */
	public static int bigEndianConvertToInt(byte[] data, int byteLen) {
		int result = 0;
		if (byteLen == 2) {
			result = (
					((data[0] & 0x000000ff)) |
							((((data[1] & 0x000000ff) << 8))));
		} else if (byteLen == 4) {
			result = (
					((data[0] & 0x000000ff)) |
							((((data[1] & 0x000000ff) << 8))) |
							((((data[2] & 0x000000ff) << 16))) |
							((((data[3] & 0x000000ff) << 24))));
		}
		return result;
	}

	/**
	 * CrcCall 16
	 * Uses irreducible polynomial:  1 + x^2 + x^15 + x^16
	 *
	 * @param data
	 * @return
	 */
	public static String CrcCall(byte[] data) {
		int[] table = {
				0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
				0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
				0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
				0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
				0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
				0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
				0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
				0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
				0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
				0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
				0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
				0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
				0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
				0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
				0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
				0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
				0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
				0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
				0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
				0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
				0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
				0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
				0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
				0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
				0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
				0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
				0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
				0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
				0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
				0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
				0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
				0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
		};

		int crc = 0x0000;
		for (byte b : data) {
			crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
		}
		String crcResult = bytes2HexString(bigEndianConvert(crc, 2));
		Log.e(TAG , "CRC16: " + crcResult);
		return crcResult;
	}

	/**
	 * Int值转换数组，高低位转换
	 *
	 * @param x
	 * @return
	 */
	public static final byte[] bigEndianConvert(int x, int byteLen) {
		byte[] result = new byte[byteLen];
		if (byteLen == 2) {
			result = new byte[]{(byte) (x & 0x000000ff),
					(byte) ((x >> 8) & 0x000000ff)};
		} else if (byteLen == 4) {
			result = new byte[]{
					(byte) (x & 0x000000ff),
					(byte) ((x >> 8) & 0x000000ff),
					(byte) ((x >> 16) & 0x000000ff),
					(byte) ((x >> 24) & 0x000000ff)
			};
		}
		return result;
	}

	public static int comparabytes(byte[] temp1, byte[] temp2, int len) {
		for (int i = 0; i < len; ++i) {
			if (temp1[i] != temp2[i]) {
				return -1;
			}
		}
		return 0;
	}
}

