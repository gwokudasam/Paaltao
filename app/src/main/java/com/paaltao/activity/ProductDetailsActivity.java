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
import com.paaltao.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.paaltao.extras.urlEndPoints.PRODUCT_DETAILS;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{

    TextView shipping,reviews;
    Menu badge_menu;
    MenuItem badge_item_cart;
    int badge_item_id_cart;
    View target_cart;
    BadgeView badge_cart;
    String accessToken = "67drd56g",productId;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        SliderLayout mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_launcher);
        this.setSupportActionBar(toolbar);



        HashMap<String,String> url_maps = new HashMap<>();
        url_maps.put("Hannibal", "http://www.paaltao.com/media/catalog/product/cache/1/image/1200x1200/9df78eab33525d08d6e5fb8d27136e95/i/m/img_1417.jpg");
        url_maps.put("Big Bang Theory", "http://www.paaltao.com/media/catalog/product/cache/1/image/1200x1200/9df78eab33525d08d6e5fb8d27136e95/i/m/img_1167.jpg");
        url_maps.put("House of Cards", "http://www.paaltao.com/media/catalog/product/cache/1/image/1200x1200/9df78eab33525d08d6e5fb8d27136e95/i/m/img_1189.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");


        for(String name : url_maps.keySet()){
            DefaultSliderView sliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            sliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mDemoSlider.addSlider(sliderView);
        }
        mDemoSlider.stopAutoCycle();
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(5000);

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
                badge_cart.setText("1");
                badge_cart.setTextColor(Color.parseColor("#ffffff"));


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
               // productArrayList = parseJsonResponse(jsonObject);
                Log.e("productArray", productArrayList.toString());

                Log.e("url",UAT_BASE_URL+PRODUCT_LIST);
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



    public void initialize() // Method to initialize all variables
    {
        shipping = (TextView)findViewById(R.id.shipping);
        reviews = (TextView)findViewById(R.id.share_product);

    }

    public void onItemClick()
    {
        shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(ProductDetailsActivity.this).title("Shipping").sheet(R.id.about_shop,"Hello World").show();
            }
        });




    }

}
