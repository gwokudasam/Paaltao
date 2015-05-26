package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.cocosw.bottomsheet.BottomSheet;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.mrengineer13.snackbar.SnackBar;
import com.paaltao.R;
import com.paaltao.classes.BadgeView;
import com.paaltao.classes.Product;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGES;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_QUANTITY;
import static com.paaltao.extras.Keys.ProductList.KEY_REVIEWS;
import static com.paaltao.extras.Keys.ProductList.KEY_SHIPPING_DETAILS;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ACCESS_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_DATA;
import static com.paaltao.extras.Keys.UserCredentials.KEY_EMAIL;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_ERROR_NODE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_FIRST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_HAS_SHOP;
import static com.paaltao.extras.Keys.UserCredentials.KEY_LAST_NAME;
import static com.paaltao.extras.Keys.UserCredentials.KEY_MESSAGE;
import static com.paaltao.extras.Keys.UserCredentials.KEY_SIGN_IN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_TOKEN;
import static com.paaltao.extras.Keys.UserCredentials.KEY_VENDOR;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_DETAILS;
import static com.paaltao.extras.urlEndPoints.PRODUCT_DETAILS;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{

    TextView shipping,reviews,productName,productPrice,productDescription,shopName,item_quantity;
    ImageView addItem,removeitem;
    Menu badge_menu;
    Intent intent;
    Button addToCart;
    int value = 0;
    SharedPreferenceClass preferenceClass;
    MenuItem badge_item_cart;
    int badge_item_id_cart;
    View target_cart;
    BadgeView badge_cart;
    SliderLayout mDemoSlider;
    String accessToken,productId,quantity,shippingDetails,productImageURL,Name,description,price,shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        preferenceClass = new SharedPreferenceClass(this);
        accessToken = preferenceClass.getAccessToken();
        Log.e("token",accessToken);

        intent = getIntent();
        productId = intent.getStringExtra("productId");
        Name = intent.getStringExtra("productName");
        description = intent.getStringExtra("description");
        price = intent.getStringExtra("productPrice");
        shop = intent.getStringExtra("shopName");




        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_launcher);
        this.setSupportActionBar(toolbar);

        sendJsonRequest();

        initialize();
        onItemClick();
       
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_details, menu);



        badge_menu = menu;

        badge_item_cart = menu.findItem(R.id.cart_icon);
        badge_item_id_cart = badge_item_cart.getItemId();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                target_cart = findViewById(badge_item_id_cart);
                badge_cart = new BadgeView(ProductDetailsActivity.this,
                        target_cart);
                badge_cart.setText("0");
                badge_cart.setTextColor(Color.parseColor("#ffffff"));
                badge_cart.setBadgeBackgroundColor(getResources().getColor(R.color.teal));


            }
        }, 1000);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_icon) {
            Intent intent = new Intent(ProductDetailsActivity.this,CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + PRODUCT_DETAILS;

    }

    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject productDetails = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("id",productId);
            productDetails.put("getProductDetails",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),productDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                parseJSONResponse(jsonObject);

                Log.e("error", jsonObject.toString());
                Log.e("id",productId);
                Log.e("json", productDetails.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(ProductDetailsActivity.this)
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

    public void parseJSONResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        try {
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            JSONObject productDetailsObject = dataObject.getJSONObject(KEY_PRODUCT_DETAILS);
            JSONObject reviewsObject = productDetailsObject.getJSONObject(KEY_REVIEWS);
            JSONArray productImagesArray = productDetailsObject.getJSONArray(KEY_PRODUCT_IMAGES);
            for (int i = 0;i<productImagesArray.length();i++)
            {
                productImageURL = productImagesArray.getString(i);
                DefaultSliderView sliderView = new DefaultSliderView(this);
                // initialize a SliderLayout
                sliderView
                        .image(productImageURL)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mDemoSlider.addSlider(sliderView);
            }
            mDemoSlider.stopAutoCycle();
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            JSONObject errorNodeObject = dataObject.getJSONObject(KEY_ERROR_NODE);


            quantity = productDetailsObject.getString(KEY_PRODUCT_QUANTITY);
            shippingDetails = productDetailsObject.getString(KEY_SHIPPING_DETAILS);
            quantity = productDetailsObject.getString(KEY_PRODUCT_QUANTITY);


            productName.setText(Name);
            productDescription.setText(description);
            productPrice.setText(price);
            shopName.setText(shop);
            if (description != null){
            Log.e("details",description);}


            value = Double.valueOf(quantity).intValue();


            String errorCode = errorNodeObject.getString(KEY_ERROR_CODE);
            String message = errorNodeObject.getString(KEY_MESSAGE);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void initialize() // Method to initialize all variables
    {
        shipping = (TextView)findViewById(R.id.shipping);
        reviews = (TextView)findViewById(R.id.share_product);
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.product_price);
        productDescription = (TextView)findViewById(R.id.product_description);
        addToCart = (Button)findViewById(R.id.add_cart);
        shopName = (TextView)findViewById(R.id.shop_name);
        item_quantity = (TextView)findViewById(R.id.quantity);
        addItem = (ImageView)findViewById(R.id.add_quantity);
        removeitem = (ImageView)findViewById(R.id.remove_quantity);

    }

    public void onItemClick()
    {
        shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(ProductDetailsActivity.this).title("Shipping").sheet(R.id.about_shop,"Hello World").show();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=1;i<= value;i++){
                    item_quantity.setText(Integer.toString(i));
                }
            }
        });

        removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badge_cart.increment(1);
                badge_cart.show();
            }
        });


    }

}
