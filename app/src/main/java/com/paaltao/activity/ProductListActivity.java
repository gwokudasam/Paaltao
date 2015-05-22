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
import android.view.View;
import android.widget.ImageView;

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
import com.paaltao.Adapters.FeaturedProductAdapter;
import com.paaltao.R;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_FEATURED_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_IS_LIKED;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_DESCRIPTION;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_PRICE;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_NAME;
import static com.paaltao.extras.urlEndPoints.FEATURED_LIST;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private JSONArray productListArray;
    ProductListActivity activity;
    FeaturedProductAdapter featuredProductAdapter;
    private ImageView favorite;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    String accessToken = "67drd56g",productName,description,imageURL,price,isLiked,catId,shopName;
    Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        sendJsonRequest();
        setContentView(R.layout.activity_product_list);
        mRecyclerView = (RecyclerView)findViewById(R.id.product_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        featuredProductAdapter = new FeaturedProductAdapter(this,activity);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(featuredProductAdapter);

        Intent intent = getIntent();
        catId = intent.getStringExtra("catId");

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
    }
    public void initialize(){

    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + PRODUCT_LIST;

    }
    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject productList = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("id",catId);
            productList.put("productList",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),productList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                productArrayList = parseJsonResponse(jsonObject);
                featuredProductAdapter.setProductArrayList(productArrayList);
                Log.e("productArray", productArrayList.toString());

                Log.e("url",UAT_BASE_URL+PRODUCT_LIST);
                Log.e("id",catId);
                Log.e("error", jsonObject.toString());
                Log.e("json", productList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(ProductListActivity.this)
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

    public ArrayList<Product> parseJsonResponse(JSONObject response) {
        ArrayList<Product> productArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_PRODUCT_LIST)) {
                    productListArray = dataObject.getJSONArray(KEY_PRODUCT_LIST);

                    Log.e("productListArray",productListArray.toString());

                    for (int i = 0; i < productListArray.length(); i++) {
                        JSONObject featuredProductObject = productListArray.getJSONObject(i);
                        id = featuredProductObject.getLong(KEY_PRODUCT_ID);
                        productName = featuredProductObject.getString(KEY_PRODUCT_NAME);
                        description = featuredProductObject.getString(KEY_PRODUCT_DESCRIPTION);
                        imageURL = featuredProductObject.getString(KEY_PRODUCT_IMAGE);
                        isLiked = featuredProductObject.getString(KEY_IS_LIKED);
                        shopName = featuredProductObject.getString(KEY_SHOP_NAME);
                        price = featuredProductObject.getString(KEY_PRODUCT_PRICE);


                        Product product = new Product();
                        product.setProduct_id(id);
                        product.setProduct_name(productName);
                        product.setPrice(price);
                        product.setIs_liked(isLiked);
                        product.setImageURL(imageURL);
                        product.setShop_name(shopName);

                        productArrayList.add(product);

                    }
                }
                if (response.has(KEY_ERROR_CODE)) {
                    JSONObject errorObject = response.getJSONObject(KEY_ERROR_CODE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productArrayList;
    }


}
