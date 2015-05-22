package com.paaltao.classes;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.paaltao.R;
import com.paaltao.activity.HomeActivity;
import com.paaltao.logging.L;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

import eu.inloop.easygcm.GcmListener;
import eu.inloop.easygcm.WakeLockRelease;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class Paaltao extends Application implements GcmListener{
    private static Paaltao sInstance;
    private static String APP_NAMESPACE = "com.paaltao";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final int NOTIFICATION_ID = 1;

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

    @Override
    public void onMessage(String s, Bundle bundle, WakeLockRelease wakeLockRelease) {
        System.out.println("### message: " + s);
        System.out.println("### bundle:");
        for (String key: bundle.keySet()) {
            System.out.println("> " + key + ": " + bundle.get(key));
        }
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Paaltao")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(s)
                                .setSummaryText(s))
                        .setContentText(s);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        wakeLockRelease.release();
    }

    @Override
    public void sendRegistrationIdToBackend(String registrationId) {
        Log.e("tokengcm",registrationId);
    }
}
