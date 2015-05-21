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
import com.paaltao.R;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.SIGN_UP;

public class SignUpActivity extends AppCompatActivity {

    Button SignInBtn;
    Button SignUpBtn;
    Context mContext;
    ProgressBar progressBar;
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

        return BASE_URL
                + SIGN_UP;

    }

    public void sendJsonRequest(){
        progressBar.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject signUp = new JSONObject();
        try {
            jsonObject.put("firstName",firstName.getText().toString());
            jsonObject.put("lastName",lastName.getText().toString());
            jsonObject.put("contactNo",contact.getText().toString());
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("password",password.getText().toString());
            signUp.put("emailSignUp",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getRequestUrl(),signUp,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }

                //Implementing Snackbar
                new SnackBar.Builder(SignUpActivity.this)
                        .withMessage(jsonObject.toString())
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
                Log.e("error",jsonObject.toString());
                Log.e("json",signUp.toString());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(SignUpActivity.this)
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
}
