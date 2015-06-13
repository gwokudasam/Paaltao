package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import static com.paaltao.extras.Keys.ProductList.KEY_EDIT_PROFILE;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.urlEndPoints.EDIT_PROFILE;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_DATA;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {
    private TextView email,changePassword;
    EditText firstName,lastName;
    SharedPreferenceClass preferenceClass;
    RelativeLayout loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Edit Profile");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        initialize();
        onItemClick();
    }

    public void initialize(){
        firstName = (EditText)findViewById(R.id.firstName_field);
        lastName = (EditText)findViewById(R.id.lastName_field);
        email= (TextView)findViewById(R.id.email_field);
        changePassword = (TextView)findViewById(R.id.change_password);
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        firstName.setText(preferenceClass.getFirstName());
        lastName.setText(preferenceClass.getLastName());
        email.setText(preferenceClass.getUserEmail());
        loadingScreen = (RelativeLayout)findViewById(R.id.loadingScreen);

    }

    public void onItemClick(){
        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(true);
            }
        });
        lastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastName.setEnabled(true);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void sendJsonRequest(){
        if (loadingScreen.getVisibility() == View.GONE){
            loadingScreen.setVisibility(View.VISIBLE);
        }
        final JSONObject jsonObject = new JSONObject();
        final JSONObject editProfile = new JSONObject();
        try{
            jsonObject.put("accessToken","67drd56g");
            jsonObject.put("user_id",preferenceClass.getCustomerId());
            jsonObject.put("user_fname",firstName.getText());
            jsonObject.put("user_lname",lastName.getText());
            jsonObject.put("email",preferenceClass.getUserEmail());
            editProfile.put("editContactInfo", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getRequestUrl(),editProfile,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (loadingScreen.getVisibility() == View.VISIBLE){
                    loadingScreen.setVisibility(View.GONE);
                }
                Log.e("error", jsonObject.toString());
                Log.e("json", editProfile.toString());


                parseJSONResponse(jsonObject);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(EditProfileActivity.this)
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
    private String getRequestUrl() {
        return UAT_BASE_URL + EDIT_PROFILE;
    }

    public void parseJSONResponse(JSONObject jsonObject){
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            if (dataObject.has(KEY_EDIT_PROFILE)){
                if (jsonObject.getJSONObject(KEY_EDIT_PROFILE) != null){
            JSONObject editProfileObject = jsonObject.getJSONObject(KEY_EDIT_PROFILE);}}
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);


            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

            if (errorCode.equals("200")){

                new SnackBar.Builder(EditProfileActivity.this)
                        .withMessage("Your profile has been updated successfully")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();

            }
            else
            {
                new SnackBar.Builder(EditProfileActivity.this)
                        .withMessage("An error occured while updating your profile")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();




        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            sendJsonRequest();
        }


        return super.onOptionsItemSelected(item);
    }


}
