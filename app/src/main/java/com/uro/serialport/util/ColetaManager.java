package com.uro.serialport.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uro.serialport.model.Coleta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para armazenar e recuperar coletas
 */
public class ColetaManager {
    private static final String TAG = "ColetaManager";
    private static final String PREF_NAME = "coleta_preferences";
    private static final String KEY_COLETAS = "coletas_list";
    
    private final SharedPreferences preferences;
    private final Gson gson;
    
    private static ColetaManager instance;
    
    private ColetaManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public static synchronized ColetaManager getInstance(Context context) {
        if (instance == null) {
            instance = new ColetaManager(context);
        }
        return instance;
    }
    
    /**
     * Salva uma coleta
     * @param coleta A coleta a ser salva
     * @return true se sucesso, false caso contrário
     */
    public boolean saveColeta(Coleta coleta) {
        try {
            List<Coleta> coletas = getColetas();
            coletas.add(coleta);
            
            SharedPreferences.Editor editor = preferences.edit();
            String json = gson.toJson(coletas);
            editor.putString(KEY_COLETAS, json);
            return editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar coleta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Recupera todas as coletas salvas
     * @return Lista de coletas
     */
    public List<Coleta> getColetas() {
        String json = preferences.getString(KEY_COLETAS, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            Type type = new TypeToken<ArrayList<Coleta>>(){}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar coletas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Limpa todas as coletas salvas
     * @return true se sucesso, false caso contrário
     */
    public boolean clearColetas() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_COLETAS);
        return editor.commit();
    }
}
