package com.paaltao.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arindam.paaltao on 21-Feb-15.
 */
public class SharedPreferenceClass {

    private static final String USER_PREFS = "PaaltaoApplication";
    private SharedPreferences sharedprefs;
    private SharedPreferences.Editor prefsEditor;

    public SharedPreferenceClass(Context context) {
        this.sharedprefs = context.getSharedPreferences(USER_PREFS,
                Activity.MODE_PRIVATE);
        this.prefsEditor = sharedprefs.edit();
    }

    public void saveLoginflag(String login_flag) {
        prefsEditor.putString("login_flag", login_flag);
        prefsEditor.commit();
    }

    public String getLoginFlag() {
        return sharedprefs.getString("login_flag", "");
    }

    public void saveUserEmail(String user_email) {
        prefsEditor.putString("user_email", user_email);
        prefsEditor.commit();
    }

    public String getUserEmail() {
        return sharedprefs.getString("user_email", "");
    }

    public void saveLastUserEmail(String last_user_email) {
        prefsEditor.putString("last_user_email", last_user_email);
        prefsEditor.commit();
    }

    public String getLastUserEmail() {
        return sharedprefs.getString("last_user_email", "");
    }

    public void saveAccessToken(String access_token) {
        prefsEditor.putString("access_token", access_token);
        prefsEditor.commit();
    }

    public String getAccessToken() {
        return sharedprefs.getString("access_token", "");
    }

    public void saveFirstName(String firstname) {
        prefsEditor.putString("firstname", firstname);
        prefsEditor.commit();
    }

    public String getFirstName() {
        return sharedprefs.getString("firstname", "");
    }

    public void saveLastName(String lastname) {
        prefsEditor.putString("lastname", lastname);
        prefsEditor.commit();
    }

    public String getLastName() {
        return sharedprefs.getString("lastname", "");
    }
}
