package com.paaltao.classes;

import android.app.Application;
import android.content.Context;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class APIManager extends Application {
    private static APIManager sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static APIManager getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
