package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.gson.JsonObject;
import com.paaltao.R;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.urlEndPoints.ADD_ADDRESS;
import static com.paaltao.extras.urlEndPoints.APP_LAUNCH;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class AddAddressActivity extends AppCompatActivity {
    EditText firstName_field,lastName_field,companyName,contactField,streetName,cityName,pincode,state,country;
    SharedPreferenceClass preferenceClass;
    String firstName,lastName,accessToken,billing="",customerId;
    CheckBox billingAddress;
    RelativeLayout loadingScreen;
    ProgressWheel progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initialize();

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Add Address");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            if (validationCheck()) {
                sendJsonRequest();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        firstName_field = (EditText)findViewById(R.id.firstName_field);
        lastName_field = (EditText)findViewById(R.id.lastName_field);
        firstName = preferenceClass.getFirstName();
        lastName = preferenceClass.getLastName();
        firstName_field.setText(firstName);
        lastName_field.setText(lastName);
        companyName = (EditText)findViewById(R.id.companyName);
        contactField = (EditText)findViewById(R.id.contact_field);
        streetName = (EditText)findViewById(R.id.street_name);
        cityName = (EditText)findViewById(R.id.city_name);
        pincode = (EditText)findViewById(R.id.pincode);
        state = (EditText)findViewById(R.id.state);
        country = (EditText)findViewById(R.id.country);
        billingAddress = (CheckBox)findViewById(R.id.defaultBilling);
        accessToken = preferenceClass.getAccessToken();
        customerId = preferenceClass.getCustomerId();
        loadingScreen = (RelativeLayout)findViewById(R.id.loadingScreen);
        progress = (ProgressWheel)findViewById(R.id.action_progress);

    }

    public void sendJsonRequest(){
        if (loadingScreen.getVisibility() == View.GONE){
            loadingScreen.setVisibility(View.VISIBLE);
        }

        final JSONObject jsonObject = new JSONObject();
        final JSONObject addAddress = new JSONObject();
        try {
            jsonObject.put("accessToken", accessToken);
            jsonObject.put("customer_id",customerId);
            jsonObject.put("f_name",firstName_field.getText());
            jsonObject.put("m_name","");
            jsonObject.put("l_name",lastName_field.getText());
            jsonObject.put("company",companyName.getText());
            jsonObject.put("contact",contactField.getText());
            jsonObject.put("street_address",streetName.getText());
            jsonObject.put("city",cityName.getText());
            jsonObject.put("pincode",pincode.getText());
            jsonObject.put("state",state.getText());
            jsonObject.put("country",country.getText());
            jsonObject.put("shipping","1");
            jsonObject.put("billing","1");
            addAddress.put("addAddress", jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(), addAddress, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (loadingScreen.getVisibility() == View.VISIBLE){
                    loadingScreen.setVisibility(View.GONE);
                }
                parseJSONResponse(jsonObject);
                L.m(addAddress.toString());

                //Calling the Snackbar
                Log.e("response", jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(AddAddressActivity.this)
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
        return UAT_BASE_URL +ADD_ADDRESS;
    }


    public void parseJSONResponse(JSONObject object){
        if (object == null || object.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = object.getJSONObject(KEY_DATA);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);

            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

            if (errorCode.contains("200")) {
                new SnackBar.Builder(AddAddressActivity.this)
                        .withMessage("Address added successfully")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();

                finish();
            }

            else{
                new SnackBar.Builder(AddAddressActivity.this)
                        .withMessage("Error in adding address")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
                }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public Boolean validationCheck(){
        if(firstName_field.getText().toString().length() == 0)
            firstName_field.setError("Please enter your first name");
        else if (lastName_field.getText().toString().length() == 0)
            lastName_field.setError("Please enter your last name");
        else if(contactField.getText().toString().length() == 0 && contactField.getText().toString().length()>10)
            contactField.setError("Please provide 10 digit contact number");
        else if(pincode.getText().toString().length() == 0)
            pincode.setError("Please provide a postal code");
        else if(streetName.getText().toString().length() == 0)
            streetName.setError("Please provide street address");
        else if(cityName.getText().toString().length() == 0)
            cityName.setError("Please provide your city name");
        else if(pincode.getText().toString().length() == 0)
            pincode.setError("Please provide a postal code");
        else return true;
        return false;
    }



}
