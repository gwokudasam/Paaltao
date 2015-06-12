package com.paaltao.activity;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paaltao.R;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_EMAIL;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_HAS_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_LAST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SELLER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_IN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_UP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_LOGIN_SUCCESS;
import static com.paaltao.extras.Keys.UserCredentials.KEY_VENDOR;
import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.SIGN_UP;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class SignUpActivity extends AppCompatActivity {

    Button SignInBtn;
    Button SignUpBtn;
    Integer size;
    List<String> xxx;
    String[] splitCookie,splitSessionId;
    Context mContext;
    String fuck,fuck_harder,userEmail,accessToken,userId,userFirstName,userLastName,finalCookie="";
    ProgressBar progressBar;
    Boolean login_success;
    SharedPreferenceClass preferenceClass;
    EditText firstName,lastName,email,contact,password,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.setSupportActionBar(toolbar);
        this.setTitle("Sign up");
        initiate();
        onItemClick();
    }


    public void initiate(){
        SignInBtn = (Button)findViewById(R.id.signInBtn);
        SignUpBtn = (Button)findViewById(R.id.signUpBtn);
        firstName = (EditText)findViewById(R.id.firstName_field);
        lastName = (EditText)findViewById(R.id.lastName_field);
        email = (EditText)findViewById(R.id.email_field);
        contact = (EditText)findViewById(R.id.contact_field);
        password = (EditText)findViewById(R.id.password_field);
        confirm_password = (EditText)findViewById(R.id.confirm_password_field);
        progressBar = (ProgressBar)findViewById(R.id.action_progress);
        preferenceClass = new SharedPreferenceClass(getApplicationContext());

    }
    public boolean validationCheck(){
        Boolean passwordCheck = password.getText().toString().equals(confirm_password.getText().toString());
        if(firstName.getText().toString().length()== 0)
            firstName.setError("Please provide your first name. Your Name must start with a capital letter");
        else if(lastName.getText().toString().length()== 0)
            lastName.setError("Please provide your last name. Your Name must start with a capital letter");
        else if (email.getText().toString().length() == 0)
            email.setError("Please provide your email. Your email must be in the format abc@xyz.com");
        else if (contact.getText().toString().length() == 0 && contact.getText().toString().length() > 10)
            contact.setError("Please provide your contact number. Your contact number must contain 10 digits");
        else if (password.getText().toString().length()==0)
            password.setError("Please provide a password");
        else if (password.getText().toString().length()<6)
            password.setError("Password should contain a minimum of 6 characters");
        else if (!passwordCheck)
            confirm_password.setError("Passwords don't match");
        else return true;
            return false;
        }

        public void onItemClick(){
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationCheck()){
                    sendJsonRequest();
                }
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
    public static String getRequestUrl() {

        return UAT_BASE_URL
                + SIGN_UP;

    }

    public void sendJsonRequest(){

        if (progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
        final JsonObject jsonObject = new JsonObject();
        final JsonObject signUpObject = new JsonObject();
        jsonObject.addProperty("firstName", firstName.getText().toString());
        jsonObject.addProperty("lastName", password.getText().toString());
        jsonObject.addProperty("email",email.getText().toString());
        jsonObject.addProperty("contactNo",contact.getText().toString());
        jsonObject.addProperty("password",password.getText().toString());
        signUpObject.add("emailSignUp", jsonObject);

        Ion.with(getApplicationContext())
                .load(getRequestUrl())
                .setJsonObjectBody(signUpObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {

                        Log.e("input_params",signUpObject.toString());
                        Log.e("response",result.getResult().toString());
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
            JsonObject signUpobject = dataObject.getAsJsonObject(KEY_SIGN_UP);
            JsonObject errorNodeObject = dataObject.getAsJsonObject(KEY_ERROR_NODE);
            if (signUpobject.has(KEY_ACCESS_TOKEN)){
                if (signUpobject.getAsJsonObject(KEY_ACCESS_TOKEN) != null){
                JsonObject accessTokenObject = signUpobject.getAsJsonObject(KEY_ACCESS_TOKEN);
                    if(accessTokenObject.has(KEY_TOKEN)){
                        if (accessTokenObject.get(KEY_TOKEN) != null)
                        accessToken = (accessTokenObject.get(KEY_TOKEN).getAsString());

                    }
                }

            }

            if (signUpobject.has(KEY_EMAIL)){
                if (signUpobject.get(KEY_EMAIL) != null){
            userEmail = (signUpobject.get(KEY_EMAIL)).getAsString();}}

            if (signUpobject.has(KEY_FIRST_NAME)){
                userFirstName = signUpobject.get(KEY_FIRST_NAME).getAsString();
            }

            if (signUpobject.has(KEY_LAST_NAME)){
                userLastName = signUpobject.get(KEY_LAST_NAME).getAsString();
            }
           // login_success = (signUpobject.get(KEY_USER_LOGIN_SUCCESS).getAsBoolean());
            if (signUpobject.has(KEY_USER_ID)){
                if (signUpobject.get(KEY_USER_ID) != null){
                userId = (signUpobject.get(KEY_USER_ID).getAsString());
                preferenceClass.saveCustomerId(userId);}}


            String errorCode = (errorNodeObject.get(KEY_ERROR_CODE).getAsString());
            if (errorNodeObject.has(KEY_MESSAGE)){
                if (errorNodeObject.get(KEY_MESSAGE) != null){
            String message = (errorNodeObject.get(KEY_MESSAGE).getAsString());}}
            if ((errorCode.contains("402"))){
                new SnackBar.Builder(SignUpActivity.this)
                        .withMessage("There is already an account with this email address")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }else if (errorCode.contains("200")){
                preferenceClass.saveAccessToken(accessToken);
                preferenceClass.saveUserEmail(userEmail);
                preferenceClass.saveFirstName(userLastName);

                Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }


            else{
                Log.e("TAG",login_success.toString());
                new SnackBar.Builder(SignUpActivity.this)
                        .withMessage("An error occured!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }


}
