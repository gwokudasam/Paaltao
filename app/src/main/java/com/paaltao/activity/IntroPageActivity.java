package com.paaltao.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Account;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.search.SearchAuthApi;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paaltao.Adapters.IntroPageAdapter;
import com.paaltao.R;
import com.paaltao.classes.DeviceInfo;
import com.paaltao.classes.Paaltao;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.extras.Keys;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import de.cketti.library.changelog.ChangeLog;
import eu.inloop.easygcm.GcmHelper;
import me.relex.circleindicator.CircleIndicator;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_FEATURED_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_IS_LIKED;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_DESCRIPTION;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_PRICE;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_EMAIL;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_HAS_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_LAST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_IN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_LOGIN_SUCCESS;
import static com.paaltao.extras.Keys.UserCredentials.KEY_VENDOR;
import static com.paaltao.extras.urlEndPoints.APP_LAUNCH;
import static com.paaltao.extras.urlEndPoints.FB_CONNECT;
import static com.paaltao.extras.urlEndPoints.GP_CONNECT;
import static com.paaltao.extras.urlEndPoints.LOGIN;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;
import static com.sromku.simple.fb.Permission.*;

public class IntroPageActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.



    public static String CLIENT_ID = "172987161234-54pat9geppejshsfu59oaqpt4argvsvb.apps.googleusercontent.com";

    ViewPager pagercontainer;
    List<String> xxx;
    ProgressWheel progressWheel;
    String[] splitCookie,splitSessionId;
    String email = "",gcm, firstName = "",fuck,finalCookie="",fuck_harder,lastName = "", gender = "",profileid,birthday,link,locale,updatedTime,emailId,userId;
    Boolean verified,login_success;
    int timeZone,size;
    Long expiryTime,currentTime,difference;
    LayoutInflater _layoutInflater;
    CircleIndicator indicator;
    TextView fbBtn,gplusBtn,signUp,signIn,sessionCheck;
    Button test;
    Person currentPerson;
    SimpleFacebook mSimpleFacebook;
    String token,gplusEmail,token1,personVerifiedEmail="",personName,personPhoto,personGooglePlusProfile,personId,personGivenName,personFamilyName,personGender,personLocale;
    SharedPreferenceClass preferenceClass;
    Context mContext;
    int heightPixels,widthPixels,densityDpi;
    float density;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    public static String username;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initialize();

        GcmHelper.init(this);
        onClick();
        appLaunchSendJson();

        gplusBtn.setOnClickListener(this);
        if (savedInstanceState != null)
        {
            mSignInProgress = savedInstanceState.getInt(
                    SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClient();


        // Initializing google plus api client



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

    //App launch service
    public void appLaunchSendJson(){

        DeviceInfo deviceInfo = new DeviceInfo();
        final JSONObject jsonObject = new JSONObject();
        final JSONObject appLaunch = new JSONObject();
        try {
            jsonObject.put("device_token", preferenceClass.getDeviceToken());
            jsonObject.put("device_name", deviceInfo.getDeviceName());
            jsonObject.put("device_type", personId);
            jsonObject.put("device_osver", personId);
            jsonObject.put("device_resolution", personId);
            jsonObject.put("device_appver", personId);
            jsonObject.put("device_lon", personId);
            jsonObject.put("device_lat", personId);

            appLaunch.put("appLaunch", jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getAppLaunchUrl(), appLaunch, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                L.m(appLaunch.toString());

                //Calling the Snackbar
                Log.e("response",jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(IntroPageActivity.this)
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private String getAppLaunchUrl() {
        return UAT_BASE_URL +APP_LAUNCH;
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
        progressWheel = (ProgressWheel)findViewById(R.id.action_progress);



    }

    public void onClick() {


        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressWheel.getVisibility() == View.GONE){
                    progressWheel.setVisibility(View.VISIBLE);
                }
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
    public void getScreenInfo(){
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        heightPixels = metrics.heightPixels;
        widthPixels = metrics.widthPixels;
        density = metrics.density;
        densityDpi = metrics.densityDpi;
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
                    if (progressWheel.getVisibility() == View.VISIBLE){
                        progressWheel.setVisibility(View.GONE);
                    }

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

                        birthday = response.getBirthday();
                        link = response.getLink();
                        updatedTime = response.getUpdatedTime();
                        timeZone = response.getTimeZone();
                        locale = response.getLocale();
                        verified = response.getVerified();
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

                    fbGetSession();
                    sendJsonRequestFacebook();
                }
            });
        }
    };


    public void fbGetSession(){
    session = Session.getActiveSession();
        if (session != null && session.getState().isOpened()){
            Log.i("sessionToken", session.getAccessToken());
            Log.i("sessionTokenDueDate", session.getExpirationDate().toString());
            expiryTime = (session.getExpirationDate().getTime());
            currentTime= System.currentTimeMillis();
            difference = ((expiryTime - currentTime)/1000);
            L.m(expiryTime.toString());
            L.m(currentTime.toString());
            L.m(String.valueOf(difference.intValue()));
        }
    }


    public void sendJsonRequestFacebook() {
        JsonObject jsonObject = new JsonObject();
        final JsonObject fbLogin = new JsonObject();
        final JsonObject jsonObject1 = new JsonObject();
        try {
            jsonObject.addProperty("id", profileid);
            jsonObject.addProperty("birthday", birthday);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("first_name", firstName);
            jsonObject.addProperty("last_name", lastName);
            jsonObject.addProperty("gender", gender);
            jsonObject.addProperty("locale", locale);
            jsonObject.addProperty("timezone", timeZone);
            jsonObject.addProperty("updated_time", updatedTime);
            jsonObject.addProperty("verified", verified);
            fbLogin.add("fbResponse", jsonObject);
            jsonObject1.addProperty("accessToken", session.getAccessToken());
            jsonObject1.addProperty("expires", String.valueOf(difference.intValue()));
            fbLogin.add("fbAccessToken", jsonObject1);

        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        Ion.with(getApplicationContext())
                .load(getRequestUrlFb())
                .setJsonObjectBody(fbLogin)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {
                        Log.e("response", result.getHeaders().getHeaders().toString());
                        Log.e("headers", result.getHeaders().getHeaders().getAll("Set-Cookie").toString().replaceAll("\\[", "").replaceAll("\\]", ""));

                        xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");

                        Log.i("bitch",result.getResult().toString());

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
                        Log.e("andy",fuck);


                    }


                });
    }

    public void parseJsonData(JsonObject jsonObject) {
        if (progressWheel.getVisibility() == View.VISIBLE){
            progressWheel.setVisibility(View.GONE);
        }
        if (jsonObject == null) {
            return;
        }
        try {
            JsonObject dataObject = jsonObject.getAsJsonObject(Keys.UserCredentials.KEY_DATA);
            JsonObject signInObject = dataObject.getAsJsonObject(KEY_SIGN_IN);
            if (signInObject.has(KEY_ACCESS_TOKEN)){
            JsonObject accessTokenObject = signInObject.getAsJsonObject(KEY_ACCESS_TOKEN);
                if(accessTokenObject.has(KEY_TOKEN)){
                    token = accessTokenObject.get(KEY_TOKEN).getAsString();}

            }
            JsonObject errorNodeObject = dataObject.getAsJsonObject(KEY_ERROR_NODE);
            if(dataObject.has(KEY_VENDOR)){
                if (dataObject.isJsonNull()){
                    return;
                }
                else {JsonObject vendorObject = dataObject.getAsJsonObject(KEY_VENDOR);
                    if(vendorObject != null){
                        String vendor_login = vendorObject.get(KEY_HAS_SHOP).getAsString();
                        if(vendor_login != null && vendor_login.contains("true")){
                            preferenceClass.saveVendorLoginSuccess(vendor_login);
                        }}}
            }

            emailId = signInObject.get(KEY_EMAIL).getAsString();
            firstName = signInObject.get(KEY_FIRST_NAME).getAsString();
            lastName = signInObject.get(KEY_LAST_NAME).getAsString();
            login_success = signInObject.get(KEY_USER_LOGIN_SUCCESS).getAsBoolean();
            userId = signInObject.get(KEY_USER_ID).getAsString();

            preferenceClass.saveCustomerId(userId);
            preferenceClass.saveFirstName(firstName);
            preferenceClass.saveLastName(lastName);
            preferenceClass.saveUserEmail(emailId);

            Log.e("bla bla bla",preferenceClass.getCustomerId());




            String errorCode = errorNodeObject.get(Keys.UserCredentials.KEY_ERROR_CODE).getAsString();
            String message = errorNodeObject.get(KEY_MESSAGE).getAsString();
            if (login_success){
                Log.e("TAG",login_success.toString());
                if (token!= null && token.length()!=0){
                    preferenceClass.saveAccessToken(token);
                    preferenceClass.saveUserEmail(emailId);

                    Intent intent = new Intent(IntroPageActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.e("TAG",login_success.toString());

            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }





    private String getRequestUrlFb() {
        return UAT_BASE_URL +FB_CONNECT;
    }

    private String getRequestUrlGplus() {
        return UAT_BASE_URL +GP_CONNECT;
    }


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

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            personName = currentPerson.getDisplayName();
            personPhoto = currentPerson.getImage().getUrl();
            personGooglePlusProfile = currentPerson.getUrl();
            personId = currentPerson.getId();
            personGivenName = currentPerson.getName().getGivenName();
            personFamilyName = currentPerson.getName().getFamilyName();
            if (currentPerson.isVerified()){
                personVerifiedEmail = "1";
            }
        }

//        if(Plus.AccountApi.getAccountName(mGoogleApiClient) != null){
//        gplusEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);}

        String scopes = "audience:server:client_id:" + CLIENT_ID;
//        try {
//           token1 =  GoogleAuthUtil.getToken(
//                    getApplicationContext(),
//                    gplusEmail, "oauth2:"
//                            + Scopes.PLUS_LOGIN + " "
//                            + Scopes.PROFILE + " https://www.googleapis.com/auth/plus.profile.emails.read");
//        } catch (IOException | GoogleAuthException e) {
//            e.printStackTrace();
//        }


        //L.m(token1+""+"gplusToken");

        mSignInProgress = STATE_DEFAULT;
        //sendGplusJsonRequest();
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





    public void sendGplusJsonRequest() {
         JsonObject jsonObject = new JsonObject();
         JsonObject gpLogin = new JsonObject();
         JsonObject jsonObject1 = new JsonObject();
        try {
            jsonObject.addProperty("id", personId);
            jsonObject.addProperty("email", "");
            jsonObject.addProperty("first_name", firstName);
            jsonObject.addProperty("verified_email", personVerifiedEmail);
            jsonObject.addProperty("given_name", personGivenName);
            jsonObject.addProperty("family_name", personFamilyName);
            jsonObject.addProperty("link", personGooglePlusProfile);
            jsonObject.addProperty("picture", personPhoto);
            jsonObject.addProperty("gender", personGender);
            gpLogin.add("gpResponse", jsonObject);


        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        Ion.with(getApplicationContext())
                .load(getRequestUrlFb())
                .setJsonObjectBody(gpLogin)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {
                        Log.e("response", result.getHeaders().getHeaders().toString());
                        Log.e("headers", result.getHeaders().getHeaders().getAll("Set-Cookie").toString().replaceAll("\\[", "").replaceAll("\\]", ""));

                        xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");


                        parseGplusJSONResponse(result.getResult());

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
                        Log.e("andy",fuck);


                    }


                });


    }

    public void parseGplusJSONResponse(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        try {
            JsonObject dataObject = jsonObject.getAsJsonObject(Keys.UserCredentials.KEY_DATA);
            JsonObject signInObject = dataObject.getAsJsonObject(KEY_SIGN_IN);
            JsonObject accessTokenObject = signInObject.getAsJsonObject(KEY_ACCESS_TOKEN);
            JsonObject errorNodeObject = dataObject.getAsJsonObject(KEY_ERROR_NODE);
            if(dataObject.has(KEY_VENDOR)){
                if (dataObject.isJsonNull()){
                    return;
                }
                else {JsonObject vendorObject = dataObject.getAsJsonObject(KEY_VENDOR);
                    if(vendorObject != null){
                        String vendor_login = vendorObject.get(KEY_HAS_SHOP).getAsString();
                        if(vendor_login != null && vendor_login.contains("true")){
                            preferenceClass.saveVendorLoginSuccess(vendor_login);
                        }}}
            }

            emailId = signInObject.get(KEY_EMAIL).getAsString();
            firstName = signInObject.get(KEY_FIRST_NAME).getAsString();
            lastName = signInObject.get(KEY_LAST_NAME).getAsString();
            login_success = signInObject.get(KEY_USER_LOGIN_SUCCESS).getAsBoolean();
            userId = signInObject.get(KEY_USER_ID).getAsString();

            preferenceClass.saveCustomerId(userId);
            preferenceClass.saveFirstName(firstName);
            preferenceClass.saveLastName(lastName);
            preferenceClass.saveUserEmail(emailId);

            Log.e("bla bla bla",preferenceClass.getCustomerId());

            if(accessTokenObject.has(KEY_TOKEN)){
                token = accessTokenObject.get(KEY_TOKEN).getAsString();}


            String errorCode = errorNodeObject.get(Keys.UserCredentials.KEY_ERROR_CODE).getAsString();
            String message = errorNodeObject.get(KEY_MESSAGE).getAsString();
            if (login_success){
                Log.e("TAG",login_success.toString());
                if (token!= null && token.length()!=0){
                    preferenceClass.saveAccessToken(token);
                    preferenceClass.saveUserEmail(emailId);

                    Intent intent = new Intent(IntroPageActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.e("TAG",login_success.toString());
                new SnackBar.Builder(IntroPageActivity.this)
                        .withMessage("Cannot provide access!")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }




}
