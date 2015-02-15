package com.paaltao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.facebook.*;
import android.widget.Toast;

import com.paaltao.Controller.IntroPageAdapter;
import com.paaltao.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;


import java.util.Vector;


import me.relex.circleindicator.CircleIndicator;

public class IntroPageActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    ViewPager pagercontainer;
    String email = "", name = "", gender = "", profileid;
    LayoutInflater _layoutInflater;
    CircleIndicator indicator;
    Button fbBtn;
    Button signUp;
    Button signIn;
    SimpleFacebook mSimpleFacebook;
    Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initialize();
        onClick();
        Vector<View> pages = new Vector<View>();
        _layoutInflater = getLayoutInflater();

        View page1 = _layoutInflater.inflate(R.layout.intro_page1, null);
        View page2 = _layoutInflater.inflate(R.layout.intro_page2, null);
        View page3 = _layoutInflater.inflate(R.layout.intro_page3, null);
        View page4 = _layoutInflater.inflate(R.layout.intro_page4, null);

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);

        IntroPageAdapter adapter = new IntroPageAdapter(this, pages);
        pagercontainer.setAdapter(adapter);

        indicator.setViewPager(pagercontainer);
        indicator.setClickable(true);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }


    public void initialize() {
        pagercontainer = (ViewPager) findViewById(R.id.pagercontainer);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        fbBtn = (Button) findViewById(R.id.fb_login);
        signUp = (Button) findViewById(R.id.signUpBtn);
        signIn = (Button) findViewById(R.id.signInBtn);
        mSimpleFacebook = SimpleFacebook.getInstance(this);



    }

    public void onClick() {
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);






            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPageActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }


}
