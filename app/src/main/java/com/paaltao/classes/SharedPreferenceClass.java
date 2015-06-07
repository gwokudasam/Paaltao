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
    public void saveVendorLoginSuccess(String hasShop){
        prefsEditor.putString("has_shop",hasShop);
        prefsEditor.commit();
    }

    public String getVendorLoginSuccess(){
        return sharedprefs.getString("has_shop","");
    }

    public void saveCustomerId(String customerId){
        prefsEditor.putString("customerId",customerId);
        prefsEditor.commit();
    }
    public String getCustomerId(){
        return sharedprefs.getString("customerId","");
    }
    public void saveCookie(String cookieValue){
        prefsEditor.putString("sessionid",cookieValue);
        prefsEditor.commit();
    }
    public void saveCookiee(String cookieValue){
        prefsEditor.putString("Cookie",cookieValue);
        prefsEditor.commit();
    }
    public String getCookie(){
        return sharedprefs.getString("sessionid","");
    }

    public void saveLoginflag(Boolean login_flag) {
        prefsEditor.putBoolean("login_flag", login_flag);
        prefsEditor.commit();
    }
    public void checkApiVer(String api_ver) {
        prefsEditor.putString("api_ver", api_ver);
        prefsEditor.commit();
    }
    public String getCheckApiVer() {
        return sharedprefs.getString("api_ver","");
    }

    public Boolean getLoginFlag() {
        return sharedprefs.getBoolean("login_flag", false);
    }



    public void saveUserEmail(String user_email) {
        prefsEditor.putString("user_email", user_email);
        prefsEditor.commit();
    }

    public String getUserEmail() {
        return sharedprefs.getString("user_email", "");
    }

    public void clearFirstName(){
        prefsEditor.remove("firstname");
        prefsEditor.commit();
    }

    public void clearLastName(){
        prefsEditor.remove("lastname");
        prefsEditor.commit();
    }

    public void clearUserEmail(){
        prefsEditor.remove("user_email");
        prefsEditor.commit();
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

    public void clearAccessToken(){
        prefsEditor.remove("access_token");
        prefsEditor.commit();
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
