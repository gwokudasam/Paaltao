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
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.paaltao.Controller.IntroPageAdapter;
import com.paaltao.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.util.Vector;

import me.relex.circleindicator.CircleIndicator;

public class IntroPageActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    ViewPager pagercontainer;
    String email = "", firstName = "",lastName = "", gender = "", profileid;
    LayoutInflater _layoutInflater;
    CircleIndicator indicator;
    Button fbBtn;
    Button gplusBtn;
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


    public void initialize() {
        pagercontainer = (ViewPager) findViewById(R.id.pagercontainer);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        fbBtn = (Button) findViewById(R.id.fb_login);
        signUp = (Button) findViewById(R.id.signUpBtn);
        signIn = (Button) findViewById(R.id.signInBtn);
        gplusBtn = (Button)findViewById(R.id.google_login);
        mSimpleFacebook = SimpleFacebook.getInstance(this);



    }

    public void onClick() {
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSimpleFacebook.login(onLoginListener);

            }
        });

        gplusBtn.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onResume() {
        AppEventsLogger.activateApp(this);
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    OnLoginListener onLoginListener = new OnLoginListener() {
        @Override
        public void onLogin() {
            mSimpleFacebook.getProfile(new OnProfileListener() {
                @Override
                public void onComplete(Profile response) {
                    try {
                        email = response.getEmail();
                        firstName = response.getFirstName();
                        lastName = response.getLastName();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        Log.e("email",email);
                        Log.e("firstname",firstName);

                    }
                    catch (Exception e) {
                        // TODO: handle exception

                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    super.onException(throwable);
                }

                @Override
                public void onFail(String reason) {
                    super.onFail(reason);
                }

                @Override
                public void onThinking() {
                    super.onThinking();
                }
            });
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {

        }

        @Override
        public void onThinking() {

        }

        @Override
        public void onException(Throwable throwable) {

        }

        @Override
        public void onFail(String s) {

        }
    };


}
