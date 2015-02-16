package com.paaltao.classes;

import android.app.Application;
import android.content.Context;

import com.paaltao.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class Paaltao extends Application {
    private static Paaltao sInstance;
    private static String APP_NAMESPACE = "com.paaltao";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Logger.DEBUG_WITH_STACKTRACE = true;

        Permission[] permissions = new Permission[] { Permission.EMAIL,
                Permission.PUBLIC_PROFILE };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.facebook_app_id))
                .setNamespace(APP_NAMESPACE).setPermissions(permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }

    public static Paaltao getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
