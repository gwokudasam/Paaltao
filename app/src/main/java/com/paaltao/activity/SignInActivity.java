package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paaltao.R;
import com.paaltao.network.VolleySingleton;

import org.json.JSONObject;

public class SignInActivity extends ActionBarActivity {
    Button SignUpBtn;
    Button SignInBtn;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initiate();
        onItemClick();
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("Sign In");
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);

        //TODO initiate service call
//        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
//        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,"www.google.com",null,new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//            }
//        },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });

    }

    public void initiate(){
        SignUpBtn = (Button)findViewById(R.id.signUpBtn);
        email = (EditText)findViewById(R.id.email_field);
        password = (EditText)findViewById(R.id.password_field);
        SignInBtn = (Button)findViewById(R.id.signInBtn);

    }
    public boolean validationCheck(){
        if (email.getText().toString().length() == 0)
            email.setError("Please provide your email. Your email must be in the format abc@xyz.com");
        else if (password.getText().toString().length()==0)
            password.setError("Please provide a password");
        else return true;
        return false;
    }
    public void onItemClick(){
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationCheck()){
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }

            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
