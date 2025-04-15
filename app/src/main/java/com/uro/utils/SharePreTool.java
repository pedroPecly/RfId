package com.uro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePreTool {
	private static Context context;
	
	
	/**
	 * 初始化
	 * @param ctx 上下文
	 */
	public static void init(Context ctx) {
		context = ctx;
	}
	
	
	/**
	 * 获取配置参数 String
	 * 
	 * @return
	 */
	public static String getString( String name,
                                     String defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {

			return preferences.getString(name, defValues);
		}
	}

	/*
	 * String 设置配置参数
	 */
	public static boolean setString( String name, String values) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(name, values);
	return 	editor.commit();
	}

	/*
	 * int,获取配置参数
	 */
	public static int getInt( String name,
                               int defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getInt(name, defValues);
		}
	}

	/*
	 * int,设置参数
	 */
	public static void setInt( String name, int value) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putInt(name, value);
			editor.commit();
		}
	}

	/*
	 * boolean类型,获取配置参数
	 */
	public static boolean getBoolean( String name,
                                       boolean defValues) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getBoolean(name, defValues);
		}
	}

	/*
	 * boolean类型,设置参数
	 */
	public static void setBoolean(String name,
                                    boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putBoolean(name, value);
			editor.commit();
		}
	}
}
