package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.paaltao.extras.Keys.UserCredentials.*;
import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.LOGIN;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class SignInActivity extends ActionBarActivity {
    Button SignUpBtn;
    Button SignInBtn;
    ProgressWheel progressBar;
    EditText email, password;
    TextView forgotPassword;
    String emailId,accessToken,api_ver,token;
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

                if(progressBar.getVisibility()== View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
                parseJSONResponse(jsonObject);
                //Calling the Snackbar
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressBar.getVisibility()== View.VISIBLE){
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
        });
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
            JSONObject vendorObject = dataObject.getJSONObject(KEY_VENDOR);
                if(vendorObject != null){
                String vendor_login = vendorObject.getString(KEY_HAS_SHOP);
                if(vendor_login != null && vendor_login.contains("true")){
                    preferenceClass.saveVendorLoginSuccess(vendor_login);
                }}
            }
            emailId = signInObject.getString(KEY_EMAIL);
            if(accessTokenObject.has(KEY_TOKEN)){
            token = accessTokenObject.getString(KEY_TOKEN);}


            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);




            if (token!= null && token.length()!=0){
                preferenceClass.saveAccessToken(token);
                preferenceClass.saveUserEmail(emailId);

                Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else {
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
