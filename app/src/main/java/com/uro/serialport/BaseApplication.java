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
    private final  String TAG  = BaseApplication.class.getSimpleName();
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
    @Override
    public void onTerminate() {
        // 程序终止
        super.onTerminate();
    }
    
    
    
    /**
    
    
    }
    /**
     * 初始化应用
     */
    public void initApplication() {
        if (!isInited) {
            SharePreTool.init(this);
            isInited = true;
        }
    }
    
    
    public static BaseApplication getInstance() {
        return mInstance;
    }
    

    public static Context getContext() {
        return mContext;
    }
    
}
