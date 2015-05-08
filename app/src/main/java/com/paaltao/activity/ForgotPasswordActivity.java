package com.paaltao.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paaltao.R;

public class ForgotPasswordActivity extends ActionBarActivity {
    private Button resetPwd;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("Forgot Password");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        initialize();
        onItemClick();
    }

    public void initialize(){
        email = (EditText)findViewById(R.id.email_field);
        resetPwd = (Button)findViewById(R.id.resetPasswordBtn);
    }
    public void onItemClick(){
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
            }
        });
    }
    public boolean validationCheck(){
        if (email.getText().toString().length() == 0)
            email.setError("Please provide your email. Your email must be in the format abc@xyz.com");
        else return true;
        return false;
    }
}
