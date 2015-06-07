package com.paaltao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.paaltao.R;
import com.paaltao.classes.MyApp;
import com.paaltao.classes.PersistentCookieStore;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.paaltao.extras.Keys.UserCredentials.*;
import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.LOGIN;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class SignInActivity extends AppCompatActivity {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";
    Button SignUpBtn;
    Button SignInBtn;
    ProgressWheel progressBar;
    EditText email, password;
    TextView forgotPassword;
    String emailId,accessToken,api_ver,token,firstName,lastName,cookie,newCookie,userId;
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
                    sendJsonRequest();
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
                preferenceClass.saveCookiee(response.headers.get("Set-Cookie"));
                cookie = response.headers.get("Set-Cookie");
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                newCookie = splitSessionId[1];
                //cookie = response.headers.values().toString();
                Log.e("split",newCookie);
                preferenceClass.saveCookie(newCookie);
                return super.parseNetworkResponse(response);
                }
                @Override
                public Map<String, String> getHeaders ()throws AuthFailureError {
                    Map<String, String> headers = super.getHeaders();

                    if (headers == null
                            || headers.equals(Collections.emptyMap())) {
                        headers = new HashMap<String, String>();
                    }

//                MyApp.get().addSessionCookie(headers);

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
                if(vendor_login != null && vendor_login.contains("true")){
                    preferenceClass.saveVendorLoginSuccess(vendor_login);
                }}}
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

            if(accessTokenObject.has(KEY_TOKEN)){
            token = accessTokenObject.getString(KEY_TOKEN);}



            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);
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
