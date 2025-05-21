package com.uro.serialport;

import android.app.Application;
import android.content.Context;

import com.uro.utils.SharePreTool;


/**
 * @author wrc
 * @version 1.0
 * @since 2019/6/3
 */
public class BaseApplication extends Application {
    private final String TAG = BaseApplication.class.getSimpleName();
    private static Context mContext;
    private static BaseApplication mInstance = null;
    private boolean isInited = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        initApplication();
    }
    
    /**
     * onTerminate não é garantido que será chamado no Android
     * Esta função é apenas para emuladores e não deve ser usada em casos reais
     */
    @Override
    public void onTerminate() {
        // Término do programa
        super.onTerminate();
    }    /**
     * Inicializar aplicação
     */
    public void initApplication() {
        if (!isInited) {
            SharePreTool.init(this);
            isInited = true;
        }
    }
    
    /**
     * Retorna a instância singleton do BaseApplication
     * @return instância do BaseApplication
     */
    public static BaseApplication getInstance() {
        return mInstance;
    }
    
    /**
     * Retorna o contexto da aplicação
     * @return contexto da aplicação
     */
    public static Context getContext() {
        return mContext;
    }
}
