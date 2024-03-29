package com.paaltao.activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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
import com.paaltao.Adapters.AddressAdapter;
import com.paaltao.R;
import com.paaltao.classes.Address;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.paaltao.extras.Keys.UserCredentials.KEY_CITY;
import static com.paaltao.extras.Keys.UserCredentials.KEY_COMPANY;
import static com.paaltao.extras.Keys.UserCredentials.KEY_CONTACT;
import static com.paaltao.extras.Keys.UserCredentials.KEY_COUNTRY_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DEFAULT_BILLING;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DEFAULT_SHIPPING;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_ADDRESSES;
import static com.paaltao.extras.Keys.ProductList.KEY_CART_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_PRICE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_LAST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_PINCODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_REGION_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_STREET;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_FIRST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_LAST_NAME;
import static com.paaltao.extras.urlEndPoints.CART_ITEMS;
import static com.paaltao.extras.urlEndPoints.GET_ADDRESS;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.ClickListener {
    private RecyclerView mRecyclerView;
    private AddressAdapter mAdapter;
    SharedPreferenceClass preferenceClass;
    private AddressActivity activity;
    TextView editAddress,removeAddress;
    ProgressWheel progressWheel;
    private  Address address;
    Intent intent;
    CheckBox selectAddress;
    RelativeLayout proceedPayment;
    private JSONArray addressListArray;
    private ArrayList<Address> addressArrayList = new ArrayList<>();
    private String accessToken,userId,checkoutValue,firstName,lastName,company,city,state,country,country_id,region_id,contact,street,pincode;
    private int defaultShipping,defaultBilling;

    @Override
    protected void onPostResume() {
        sendJsonRequest();
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initialize();
        intent = getIntent();
        if (intent.getStringExtra("Checkout") != null){
            checkoutValue = "Select Address";
            proceedPayment.setVisibility(View.VISIBLE);

        }
        else {checkoutValue = "My Address";}

        onClick();
        sendJsonRequest();
        mRecyclerView = (RecyclerView)findViewById(R.id.address_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new AddressAdapter(getApplicationContext(),this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);


        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);

        this.setTitle("My Address");
    }

    public void sendJsonRequest(){
        if (progressWheel.getVisibility() == View.GONE){
            progressWheel.setVisibility(View.VISIBLE);
        }
        final JSONObject jsonObject = new JSONObject();
        final JSONObject addressList = new JSONObject();
        userId = preferenceClass.getCustomerId();
        accessToken = preferenceClass.getAccessToken();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("customer_id",userId);
            addressList.put("getAddress",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),addressList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (progressWheel.getVisibility() == View.VISIBLE){
                    progressWheel.setVisibility(View.GONE);
                }

                addressArrayList = parseJsonResponse(jsonObject);
                mAdapter.setAddressArrayList(addressArrayList);


                //console test
                Log.e("addressList", addressArrayList.toString());
                Log.e("input_payload",addressList.toString());
                Log.e("error", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(AddressActivity.this)
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
    public static String getRequestUrl() {

        return UAT_BASE_URL
                + GET_ADDRESS;

    }

    public ArrayList<Address> parseJsonResponse(JSONObject response) {

        ArrayList<Address> addressArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_ADDRESSES)) {
                    if(dataObject.isNull(KEY_ADDRESSES)){
                        new SnackBar.Builder(AddressActivity.this)
                                .withMessage("No Addresses Found")
                                .withTextColorId(R.color.white)
                                .withDuration((short) 10000)
                                .show();
                        //if user do not have ny address, the user will be redirected to add address activity
                        Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        addressListArray = dataObject.getJSONArray(KEY_ADDRESSES);
                        if (addressListArray != null)
                            for (int i = 0; i < addressListArray.length(); i++) {
                                L.m(String.valueOf(addressListArray.length()));
                                JSONObject addressListObject = addressListArray.getJSONObject(i);
                                if (!addressListObject.isNull(KEY_USER_FIRST_NAME)){
                                firstName = addressListObject.getString(KEY_USER_FIRST_NAME);}
                                if (!addressListObject.isNull(KEY_USER_LAST_NAME)){
                                lastName = addressListObject.getString(KEY_USER_LAST_NAME);}
                                if (!addressListObject.isNull(KEY_COMPANY)){
                                company = addressListObject.getString(KEY_COMPANY);}
                                if (addressListObject.isNull(KEY_CITY)){
                                city = addressListObject.getString(KEY_CITY);}
                                if (!addressListObject.isNull(KEY_COUNTRY_ID)){
                                country_id = addressListObject.getString(KEY_COUNTRY_ID);}
                                if (!addressListObject.isNull(KEY_REGION_ID)){
                                region_id = addressListObject.getString(KEY_REGION_ID);}
                                if (!addressListObject.isNull(KEY_PINCODE)){
                                pincode = addressListObject.getString(KEY_PINCODE);}
                                if (!addressListObject.isNull(KEY_CONTACT)){
                                contact = addressListObject.getString(KEY_CONTACT);}
                                if (!addressListObject.isNull(KEY_STREET)){
                                street = addressListObject.getString(KEY_STREET);}
                                if (!addressListObject.isNull(KEY_DEFAULT_BILLING)){
                                defaultBilling = addressListObject.getInt(KEY_DEFAULT_BILLING);}
                                if (!addressListObject.isNull(KEY_DEFAULT_SHIPPING)){
                                defaultShipping = addressListObject.getInt(KEY_DEFAULT_SHIPPING);}


                                address = new Address();
                                address.setFirstName(firstName);
                                address.setLastName(lastName);
                                address.setCompany(company);
                                address.setCity(city);
                                address.setPincode(pincode);
                                address.setContact(contact);
                                address.setStreetName(street);


                                addressArrayList.add(address);

                            }


                    }
                }
                if (response.has(KEY_ERROR_CODE)) {
                    JSONObject errorObject = response.getJSONObject(KEY_ERROR_CODE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return addressArrayList;
    }


    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        removeAddress = (TextView)findViewById(R.id.remove_address);
        editAddress = (TextView)findViewById(R.id.edit_address);
        progressWheel = (ProgressWheel)findViewById(R.id.action_progress);
        selectAddress = (CheckBox)findViewById(R.id.selectAddress);
        proceedPayment = (RelativeLayout)findViewById(R.id.proceedPayment);

    }


    public void onClick() {

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_address){
            Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void itemClicked(View view, int position) {
        Address address = addressArrayList.get(position);
        //mAdapter.remove(address);


    }
}
