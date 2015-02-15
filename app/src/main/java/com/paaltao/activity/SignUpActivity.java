package com.paaltao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paaltao.R;

public class SignUpActivity extends ActionBarActivity {

    Button SignInBtn;
    Button SignUpBtn;
    Context mContext;
    EditText name,email,contact,password,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initiate();
        onItemClick();
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("Sign Up");
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
    }


    public void initiate(){
        SignInBtn = (Button)findViewById(R.id.signInBtn);
        SignUpBtn = (Button)findViewById(R.id.signUpBtn);
        name = (EditText)findViewById(R.id.name_field);
        email = (EditText)findViewById(R.id.email_field);
        contact = (EditText)findViewById(R.id.contact_field);
        password = (EditText)findViewById(R.id.password_field);
        confirm_password = (EditText)findViewById(R.id.confirm_password_field);

    }
    public void validationCheck(){
        Boolean passwordCheck = password.getText().toString().equals(confirm_password.getText().toString());
        if(name.getText().toString().length()== 0)
            name.setError("Please provide your name");
        else if (email.getText().toString().length() == 0)
            email.setError("Please provide your email");
        else if (contact.getText().toString().length() == 0)
            contact.setError("Please provide your contact number");
        else if (password.getText().toString().length()==0)
            password.setError("Please provide a password");
        else if (!passwordCheck)
            confirm_password.setError("Passwords don't match");

        }

        public void onItemClick(){
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();



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
}
