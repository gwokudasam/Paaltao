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
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.cocosw.bottomsheet.BottomSheet;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paaltao.R;
import com.paaltao.classes.BadgeView;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.paaltao.extras.Keys.ProductList.KEY_ADD_CART;
import static com.paaltao.extras.Keys.ProductList.KEY_AVERAGE_RATING;
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
import static com.paaltao.extras.urlEndPoints.ADD_CART;
import static com.paaltao.extras.urlEndPoints.PRODUCT_DETAILS;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{

    TextView shipping,reviews,productName,productPrice,productDescription,shopName,item_quantity;
    ImageView addItem,removeitem;
    Menu badge_menu;
    Intent intent;
    Button addToCart;
    LinearLayout visitShop;
    int value = 1,rating;
    ProgressWheel progress;
    SharedPreferenceClass preferenceClass;
    MenuItem badge_item_cart;
    int badge_item_id_cart,productQty;
    View target_cart;
    BadgeView badge_cart;
    SliderLayout mDemoSlider;
    RatingBar productRating;
    Float ratingValue;
    JSONArray reviewsArray;
    JSONObject reviewsObject;
    String accessToken,productId,quantity,shippingDetails,productImageURL,Name,description,price,shop,errorCode,vendorId;

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
        vendorId = intent.getStringExtra("vendorId");




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
                badge_cart.setText(preferenceClass.getCartItem().toString());
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

    public static String getRequestURLAddCart() {

        return UAT_BASE_URL
                + ADD_CART;

    }

    public void addCartJsonRequest(){
        if (progress.getVisibility() == View.GONE){
        progress.setVisibility(View.VISIBLE);}
        JsonObject jsonObject = new JsonObject();
        JsonObject addToCart = new JsonObject();
        jsonObject.addProperty("accessToken",accessToken);
        jsonObject.addProperty("product_id",productId);
        jsonObject.addProperty("qty",item_quantity.getText().toString());
        addToCart.add("addToCart", jsonObject);


        Ion.with(getApplicationContext())
                .load(getRequestURLAddCart())
                .setJsonObjectBody(addToCart)
                .asJsonObject()
               .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.e("response",result.toString());
                        parseCartJsonResponse(result);
                    }

                });





    }


    public void parseCartJsonResponse(JsonObject object){
        if (progress.getVisibility() == View.VISIBLE){
            progress.setVisibility(View.GONE);
        }
        if (object == null ) {
            return;
        }
        try {
            JsonObject dataObject = object.getAsJsonObject(KEY_DATA);
            JsonObject errorNodeObject = dataObject.getAsJsonObject(KEY_ERROR_NODE);

            String errorCode = errorNodeObject.get(KEY_ERROR_CODE).getAsString();
            String message = errorNodeObject.get(KEY_MESSAGE).getAsString();

            if (message.contains("Product added successfully")){
                badge_cart.increment(Integer.parseInt(item_quantity.getText().toString()));
                badge_cart.show();
                preferenceClass.saveCartItem(Integer.parseInt(item_quantity.getText().toString()));
                new SnackBar.Builder(ProductDetailsActivity.this)
                        .withMessage("Product has been successfully added to cart")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }else{
                new SnackBar.Builder(ProductDetailsActivity.this)
                        .withMessage("The requested product is unavailable")
                        .withTextColorId(R.color.white)
                        .withDuration((short) 6000)
                        .show();
            }

        } catch (JsonIOException e) {
            e.printStackTrace();
        }


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
            if (productDetailsObject.has(KEY_REVIEWS)){
                if (!productDetailsObject.isNull(KEY_REVIEWS)){
            reviewsObject = productDetailsObject.getJSONObject(KEY_REVIEWS);
                if (reviewsObject.has(KEY_REVIEWS)){
                    if (!reviewsObject.isNull(KEY_REVIEWS)){
                        reviewsArray = reviewsObject.getJSONArray(KEY_REVIEWS);
                    }
                }
                }}
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
            if (productDetailsObject.has(KEY_SHIPPING_DETAILS)){
                if (productDetailsObject.isNull(KEY_SHIPPING_DETAILS)){
                    shippingDetails = "Not Available";
                }else{
                    shippingDetails = productDetailsObject.getString(KEY_SHIPPING_DETAILS);
                }
            }else{
                shippingDetails = "Not Available";
            }

            quantity = productDetailsObject.getString(KEY_PRODUCT_QUANTITY);
            productQty = Integer.parseInt(quantity);




            rating = reviewsObject.getInt(KEY_AVERAGE_RATING);
            ratingValue =  (float)((rating*20)/100);
            //productRating.setRating(ratingValue);

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
        progress = (ProgressWheel)findViewById(R.id.action_progress);
        visitShop = (LinearLayout)findViewById(R.id.visitShop);
       //productRating = (RatingBar)findViewById(R.id.ratingBar);

    }

    public void onItemClick()
    {
        shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(ProductDetailsActivity.this).title("Shipping Information").sheet(R.id.about_shop,shippingDetails).show();
            }
        });


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             int a = Integer.parseInt(item_quantity.getText().toString());
               if (a < productQty){
                   a++;
                   item_quantity.setText(String.valueOf(a));
               }
                else{
                   new SnackBar.Builder(ProductDetailsActivity.this)
                           .withMessage("You have reached the maximum available quantity")
                           .withTextColorId(R.color.white)
                           .withDuration((short) 6000)
                           .show();
               }

            }
        });


        removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(item_quantity.getText().toString());
                if (a>1){
                    a--;
                    item_quantity.setText(String.valueOf(a));
                }

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartJsonRequest();
            }
        });


        visitShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vendorId != null){
                    Intent intent = new Intent(ProductDetailsActivity.this,ShopActivity.class);
                    intent.putExtra("vendorId",vendorId);
                    startActivity(intent);
                }

            }
        });


        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ProductDetailsActivity.this,ReviewsActivity.class);
                if (reviewsArray != null){
                    intent.putExtra("reviewsArray", reviewsArray.toString());}
                startActivity(intent);
            }
        });
    }

}
