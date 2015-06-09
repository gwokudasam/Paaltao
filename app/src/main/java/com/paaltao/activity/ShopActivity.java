package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.paaltao.R;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.extras.Keys;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_CATEGORY_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_EMAIL;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_HAS_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_LAST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SELLER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_IN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_ID;
import static com.paaltao.extras.Keys.UserCredentials.KEY_USER_LOGIN_SUCCESS;
import static com.paaltao.extras.Keys.UserCredentials.KEY_VENDOR;
import static com.paaltao.extras.urlEndPoints.CART_ITEMS;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_DETAILS;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;
import static com.paaltao.extras.urlEndPoints.VIEW_SHOP;

public class ShopActivity extends AppCompatActivity {
    SharedPreferenceClass preferenceClass;
    private RelativeLayout viewProducts;
    Long cat_id;
    ImageView shopCoverImage;
    TextView shop_name,shop_details;
    String name = "",sellerId,shopCategoryId,shopName,shopImageURL,accessToken,shopStory;
    private VolleySingleton singleton;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initialize();
        sendJsonRequest();
        onClick();
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setSupportActionBar(toolbar);
        this.setTitle("Shop");


    }

    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject shopDetails = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("id",sellerId);
            shopDetails.put("getShopDetails",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),shopDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                parseJSONResponse(jsonObject);
                //console test
                Log.e("input_payload",shopDetails.toString());
                Log.e("response", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(ShopActivity.this)
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
                + VIEW_SHOP;

    }

    public void parseJSONResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            JSONObject shopDetails = dataObject.getJSONObject(KEY_SHOP_DETAILS);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);

            if (shopDetails.has(KEY_SHOP_CATEGORY_ID)){
                if (shopDetails.isNull(KEY_SHOP_CATEGORY_ID)){
                    return;
                }
               shopCategoryId =  shopDetails.getString(KEY_SHOP_CATEGORY_ID);
            }
            if (shopDetails.has(KEY_SHOP_IMAGE)){
                if (shopDetails.getString(KEY_SHOP_IMAGE).contains("false")){
                    return;
                }
                shopImageURL = shopDetails.getString(KEY_SHOP_IMAGE);
                imageLoader.get(shopImageURL, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                       shopCoverImage.setImageBitmap(imageContainer.getBitmap());
                        Log.e("imageURLAdapter",shopImageURL);

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("imageURL","no image found");

                    }
                });
            }
            if (shopDetails.has(KEY_SHOP_DETAILS)){
                if (shopDetails.isNull(KEY_SHOP_DETAILS)){
                    return;
                }
                shopStory = shopDetails.getString(KEY_SHOP_DETAILS);
                shop_details.setText(shopStory);

            }
            if (shopDetails.has(KEY_SHOP_NAME)){
                if (shopDetails.isNull(KEY_SHOP_NAME)){
                    return;
                }
                shopName = shopDetails.getString(KEY_SHOP_NAME);
                shop_name.setText(shopName);

            }

            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        accessToken = preferenceClass.getAccessToken();
        viewProducts = (RelativeLayout)findViewById(R.id.view_products);
        sellerId = preferenceClass.getSellerId();
        shop_name = (TextView)findViewById(R.id.shop_name);
        shop_details = (TextView)findViewById(R.id.shopDetails);
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
        shopCoverImage = (ImageView)findViewById(R.id.shop_cover_image);
    }

    public void onClick(){
        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.m(shopCategoryId);
                Intent intent = new Intent(ShopActivity.this, ProductListActivity.class);
                intent.putExtra("catId", shopCategoryId);
                startActivity(intent);
            }
        });
    }




}
