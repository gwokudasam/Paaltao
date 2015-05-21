package com.paaltao.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.paaltao.Adapters.IntroPageAdapter;
import com.paaltao.R;
import com.paaltao.classes.SharedPreferenceClass;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.Vector;

import de.cketti.library.changelog.ChangeLog;
import me.relex.circleindicator.CircleIndicator;

import static com.sromku.simple.fb.Permission.*;

public class IntroPageActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    ViewPager pagercontainer;
    String email = "", firstName = "",lastName = "", gender = "", profileid;
    LayoutInflater _layoutInflater;
    CircleIndicator indicator;
    TextView fbBtn,gplusBtn,signUp,signIn;
    SimpleFacebook mSimpleFacebook;
    String token;
    SharedPreferenceClass preferenceClass;
    Context mContext;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    public static String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }
        initialize();
        onClick();

        gplusBtn.setOnClickListener(this);
        if (savedInstanceState != null)
        {
            mSignInProgress = savedInstanceState.getInt(
                    SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClient();


        if(token != null && token.length() != 0 ){
            Intent intent = new Intent(IntroPageActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

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
        fbBtn = (TextView) findViewById(R.id.fb_login);
        signUp = (TextView) findViewById(R.id.signUpBtn);
        signIn = (TextView) findViewById(R.id.signInBtn);
        gplusBtn = (TextView)findViewById(R.id.google_login);
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        preferenceClass = new SharedPreferenceClass(this);
        token = preferenceClass.getAccessToken();



    }

    public void onClick() {
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSimpleFacebook.login(onLoginListener);

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


    private GoogleApiClient buildGoogleApiClient()
    {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
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
        switch (requestCode)
        {
            case 0:
                if (resultCode == RESULT_OK)
                {
                    mSignInProgress = STATE_SIGN_IN;
                }
                else
                {
                    mSignInProgress = STATE_DEFAULT;
                }
                if (!mGoogleApiClient.isConnecting())
                {
                    mGoogleApiClient.connect();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    OnLoginListener onLoginListener = new OnLoginListener() {

        @Override
        public void onFail(String reason) {
            // TODO Auto-generated method stub

            Toast.makeText(IntroPageActivity.this, "Facebook Error",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onException(Throwable throwable) {
            // TODO Auto-generated method stub

            Toast.makeText(IntroPageActivity.this, "Facebook Error",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onThinking() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNotAcceptingPermissions(Type type) {
            Toast.makeText(IntroPageActivity.this, "You need to accept the permissions",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onLogin() {
            // TODO Auto-generated method stub
            mSimpleFacebook.getProfile(new OnProfileListener() {

                public void onFail(String reason) {

                    Toast.makeText(IntroPageActivity.this, "Some error occurred in connection with Facebook",
                            Toast.LENGTH_LONG).show();

                }

                public void onException(Throwable throwable) {

                }

                public void onThinking() {

                }

                public void onComplete(
                        com.sromku.simple.fb.entities.Profile response) {

                    try {

                        email = response.getEmail();
                        firstName = response.getFirstName();
                        lastName = response.getLastName();
                        gender = response.getGender();
                        profileid = response.getId();

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    preferenceClass = new SharedPreferenceClass(getApplicationContext());
                    preferenceClass.saveUserEmail(email);
                    preferenceClass.saveFirstName(firstName);
                    preferenceClass.saveLastName(lastName);
                    preferenceClass.saveLastUserEmail(email);
                    Intent intent = new Intent(IntroPageActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    };


    private void resolveSignInError()
    {
        if (mSignInIntent != null)
        {
            try
            {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(
                        mSignInIntent.getIntentSender(), 0,
                        null, 0, 0, 0);
            }
            catch (IntentSender.SendIntentException e)
            {
                Log.i("Log error",
                        "Sign in intent could not be sent: "
                                + e.getLocalizedMessage());
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Error signing in", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        gplusBtn.setEnabled(false);

        Person currentUser = Plus.PeopleApi
                .getCurrentPerson(mGoogleApiClient);
        username = currentUser.getDisplayName();
        Toast toast = Toast.makeText(getApplicationContext(),
                "Welcome " + username, Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER, 0, -150);
        toast.show();
        mSignInProgress = STATE_DEFAULT;
        Intent i = new Intent(getApplicationContext(),
                HomeActivity.class);
        i.putExtra("username", currentUser.getDisplayName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

        if (!mGoogleApiClient.isConnecting())
        {
            switch (v.getId())
            {
                case R.id.google_login:
                    resolveSignInError();
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


        if (mSignInProgress != STATE_IN_PROGRESS)
        {
            mSignInIntent = connectionResult.getResolution();
            if (mSignInProgress == STATE_SIGN_IN)
            {
                resolveSignInError();
            }
        }
    }
}
