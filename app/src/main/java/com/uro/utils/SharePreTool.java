package com.uro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePreTool {
	private static Context context;
	
	
	/**
	 * Inicialização
	 * @param ctx contexto da aplicação
	 */
	public static void init(Context ctx) {
		if (ctx != null) {
			context = ctx.getApplicationContext(); // Sempre use o contexto da aplicação
		}
	}
	
		/**
	 * Obtém parâmetro de configuração String
	 * 
	 * @param name nome do parâmetro
	 * @param defValues valor padrão
	 * @return valor do parâmetro
	 */
	public static String getString(String name, String defValues) {
		if (context == null) return defValues;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getString(name, defValues);
		}
	}

	/**
	 * Define parâmetro de configuração String
	 * 
	 * @param name nome do parâmetro
	 * @param values valor a ser salvo
	 * @return true se a operação foi bem-sucedida
	 */
	public static boolean setString(String name, String values) {
		if (context == null) return false;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(name, values);
		return editor.commit();
	}
	/**
	 * Obtém parâmetro de configuração int
	 * 
	 * @param name nome do parâmetro
	 * @param defValues valor padrão
	 * @return valor do parâmetro
	 */
	public static int getInt(String name, int defValues) {
		if (context == null) return defValues;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getInt(name, defValues);
		}
	}

	/**
	 * Define parâmetro de configuração int
	 * 
	 * @param name nome do parâmetro
	 * @param value valor a ser salvo
	 * @return true se a operação foi bem-sucedida
	 */
	public static boolean setInt(String name, int value) {
		if (context == null) return false;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putInt(name, value);
			return editor.commit();
		}
	}
	/**
	 * Obtém parâmetro de configuração boolean
	 * 
	 * @param name nome do parâmetro
	 * @param defValues valor padrão
	 * @return valor do parâmetro
	 */
	public static boolean getBoolean(String name, boolean defValues) {
		if (context == null) return defValues;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getBoolean(name, defValues);
		}
	}

	/**
	 * Define parâmetro de configuração boolean
	 * 
	 * @param name nome do parâmetro
	 * @param value valor a ser salvo
	 * @return true se a operação foi bem-sucedida
	 */
	public static boolean setBoolean(String name, boolean value) {
		if (context == null) return false;
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putBoolean(name, value);
			return editor.commit();
		}
	}
}
