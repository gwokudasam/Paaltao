package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paaltao.R;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paaltao.extras.Keys.UserCredentials.*;
import static com.paaltao.extras.urlEndPoints.LOGIN;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class SignInActivity extends AppCompatActivity {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";
    Button SignUpBtn;
    Integer size;
    Ion ion;
    List<String> xxx;
    String[] splitCookie,splitSessionId;
    Button SignInBtn;
    ProgressWheel progressBar;
    EditText email, password;
    TextView forgotPassword;
    String emailId,accessToken,api_ver,token,firstName,finalCookie = "",lastName,cookie,newCookie,userId,sellerId,abcd,fuck,fuck_harder;
    Boolean login_success;
    SharedPreferenceClass preferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.setSupportActionBar(toolbar);
        this.setTitle("Sign in");
        initiate();
        onItemClick();



    }

    public void initiate() {
        SignUpBtn = (Button) findViewById(R.id.signUpBtn);
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);
        SignInBtn = (Button) findViewById(R.id.signInBtn);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        progressBar = (ProgressWheel)findViewById(R.id.action_progress);
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        ion  = Ion.getDefault(getApplicationContext());

    }

    public boolean validationCheck() {
        if (email.getText().toString().length() == 0)
            email.setError("Please provide your email. Your email must be in the format abc@xyz.com");
        else if (password.getText().toString().length() == 0)
            password.setError("Please provide a password");
        else return true;
        return false;
    }

    public void onItemClick() {
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationCheck()) {
                    request1();
                }

            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + LOGIN;

    }



       public void request1(){

        if (progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
        JsonObject jsonObject = new JsonObject();
        final JsonObject signInObject = new JsonObject();
        jsonObject.addProperty("email", email.getText().toString());
        jsonObject.addProperty("password", password.getText().toString());
        signInObject.add("emailSignIn", jsonObject);

        Ion.with(getApplicationContext())
                .load(getRequestUrl())
                .setJsonObjectBody(signInObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {
                        //Log.e("response", result.getHeaders().getHeaders().toString());
                        //Log.e("headers", result.getHeaders().getHeaders().getAll("Set-Cookie").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                        //abcd = result.getHeaders().getHeaders().getAll("Set-Cookie").toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        //Log.e("jhghjgdf", abcd);


                        parseJsonData(result.getResult());

                        xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");
                        size = xxx.size();
                        splitCookie = xxx.get(size - 1).split(";");
                        splitSessionId = splitCookie[0].split("=");
                        fuck = splitSessionId[1];

                        for (int i=size;i>0;i--){
                            String[] splitCookie = xxx.get(i-1).split(";");
                            String[] splitSessionId = splitCookie[0].split("=");
                            fuck = splitSessionId[1];
                            if (!fuck.contentEquals("deleted")){
                                finalCookie = fuck;
                                preferenceClass.saveCookie("frontend="+finalCookie);
                                Log.e("fucku", finalCookie);
                                break;
                            }
                        }
                        Log.e("andy", fuck);


                    }


                });


    }

    public void parseJsonData(JsonObject object){
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
        if (object == null ) {
            return;
        }
        try {
            JsonObject dataObject = object.getAsJsonObject(KEY_DATA);
            JsonObject signInObject = dataObject.getAsJsonObject(KEY_SIGN_IN);
            JsonObject accessTokenObject = signInObject.getAsJsonObject(KEY_ACCESS_TOKEN);
            JsonObject errorNodeObject = dataObject.getAsJsonObject(KEY_ERROR_NODE);
            if(dataObject.has(KEY_VENDOR)){
                if (dataObject.isJsonNull()){
                    return;
                }
                else {JsonObject vendorObject = dataObject.getAsJsonObject(KEY_VENDOR);
                    if(vendorObject != null){
                        String vendor_login = (vendorObject.get(KEY_HAS_SHOP)).getAsString();
                        Log.e("hasShop",vendor_login);
                        if(vendor_login != null && vendor_login.contains("true")){
                            preferenceClass.saveVendorLoginSuccess(vendor_login);
                        }
                        if (vendorObject.has(KEY_SELLER_ID)){
                            if( vendorObject.isJsonNull()){
                                return;
                            }
                            sellerId =  (vendorObject.get(KEY_SELLER_ID)).getAsString();
                        }
                    }}
            }

            if (signInObject.isJsonNull()){
                return;
            }
            emailId = (signInObject.get(KEY_EMAIL)).getAsString();
            if (signInObject.has(KEY_FIRST_NAME)){
            firstName = (signInObject.get(KEY_FIRST_NAME).getAsString());
                preferenceClass.saveFirstName(firstName);}
            if (signInObject.has(KEY_LAST_NAME)){
            lastName = (signInObject.get(KEY_LAST_NAME).getAsString());
                preferenceClass.saveLastName(lastName);}
            login_success = (signInObject.get(KEY_USER_LOGIN_SUCCESS).getAsBoolean());
            if (signInObject.has(KEY_USER_ID)){
            userId = (signInObject.get(KEY_USER_ID).getAsString());
                preferenceClass.saveCustomerId(userId);}




            preferenceClass.saveUserEmail(emailId);
            preferenceClass.saveSellerId(sellerId);

            if(accessTokenObject.has(KEY_TOKEN)){
                token = (accessTokenObject.get(KEY_TOKEN).getAsString());}



            String errorCode = (errorNodeObject.get(KEY_ERROR_CODE).getAsString());
            String message = (errorNodeObject.get(KEY_MESSAGE).getAsString());
            if (!(errorCode.contains("200"))){
                new SnackBar.Builder(SignInActivity.this)
                        .withMessage("Username or Password is Incorrect!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
            if (login_success){
                Log.e("TAG",login_success.toString());
                if (token!= null && token.length()!=0){
                    preferenceClass.saveAccessToken(token);
                    preferenceClass.saveUserEmail(emailId);

                    Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.e("TAG",login_success.toString());
                new SnackBar.Builder(SignInActivity.this)
                        .withMessage("Username or Password is Incorrect!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }



    public void sendJsonRequest() {
        progressBar.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject signIn = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", password.getText().toString());
            signIn.put("emailSignIn", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(), signIn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                parseJSONResponse(jsonObject);
                //Calling the Snackbar
                Log.e("response", jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(SignInActivity.this)
                            .withMessage("No Internet Connection!")
                            .withTextColorId(R.color.white)
                            .withDuration((short) 6000)
                            .show();

                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {

                    //TODO
                } else if (volleyError instanceof ParseError) {

                    //TODO
                }

            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                // MyApp.get().checkSessionCookie(response.headers);
                L.m(response.headers.toString());
                L.m(Arrays.toString(response.data));

                L.m(response.headers.get("Set-Cookie"));
                cookie = response.headers.get("Set-Cookie");
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                newCookie = splitSessionId[1];
                String cookiez = response.headers.values().toString();
                Log.e("split",newCookie);
                Log.e("split",cookiez);
                preferenceClass.saveCookie("frontend="+newCookie);

                return super.parseNetworkResponse(response);
                }
                @Override
                public Map<String, String> getHeaders ()throws AuthFailureError {
                    Map<String, String> headers = super.getHeaders();

                    if (headers == null
                            || headers.equals(Collections.emptyMap())) {
                        headers = new HashMap<String, String>();
                    }


                    return headers;
                }
            }

            ;
            requestQueue.add(jsonObjectRequest);
        }

    public void parseJSONResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            JSONObject signInObject = dataObject.getJSONObject(KEY_SIGN_IN);
            JSONObject accessTokenObject = signInObject.getJSONObject(KEY_ACCESS_TOKEN);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);
            if(dataObject.has(KEY_VENDOR)){
                if (dataObject.isNull(KEY_VENDOR)){
                    return;
                }
            else {JSONObject vendorObject = dataObject.getJSONObject(KEY_VENDOR);
                if(vendorObject != null){
                String vendor_login = vendorObject.getString(KEY_HAS_SHOP);
                    Log.e("hasShop",vendor_login);
                if(vendor_login != null && vendor_login.contains("true")){
                    preferenceClass.saveVendorLoginSuccess(vendor_login);
                }
                if (vendorObject.has(KEY_SELLER_ID)){
                  if( vendorObject.isNull(KEY_SELLER_ID)){
                        return;
                    }
                  sellerId =  vendorObject.getString(KEY_SELLER_ID);
                }
                }}
            }

            emailId = signInObject.getString(KEY_EMAIL);
            firstName = signInObject.getString(KEY_FIRST_NAME);
            lastName = signInObject.getString(KEY_LAST_NAME);
            login_success = signInObject.getBoolean(KEY_USER_LOGIN_SUCCESS);
            userId = signInObject.getString(KEY_USER_ID);

            preferenceClass.saveCustomerId(userId);
            preferenceClass.saveFirstName(firstName);
            preferenceClass.saveLastName(lastName);
            preferenceClass.saveUserEmail(emailId);
            preferenceClass.saveSellerId(sellerId);

            if(accessTokenObject.has(KEY_TOKEN)){
            token = accessTokenObject.getString(KEY_TOKEN);}



            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);
            if (!(errorCode.contains("200"))){
                new SnackBar.Builder(SignInActivity.this)
                        .withMessage("Username or Password is Incorrect!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
            if (login_success){
                Log.e("TAG",login_success.toString());
                if (token!= null && token.length()!=0){
                    preferenceClass.saveAccessToken(token);
                    preferenceClass.saveUserEmail(emailId);

                    Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.e("TAG",login_success.toString());
                new SnackBar.Builder(SignInActivity.this)
                        .withMessage("Username or Password is Incorrect!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
