package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.paaltao.Adapters.CartAdapter;
import com.paaltao.R;
import com.paaltao.classes.Category;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.paaltao.extras.Keys.ProductList.KEY_CART_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_CATEGORY_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_CATEGORY_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_CATEGORY_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_CATEGORY_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_PRICE;
import static com.paaltao.extras.urlEndPoints.CART_ITEMS;
import static com.paaltao.extras.urlEndPoints.CATEGORY_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class CartActivity extends AppCompatActivity implements CartAdapter.ClickListener{
    private RecyclerView mRecyclerView;
    SharedPreferenceClass preferenceClass;
    String accessToken,imageURL,productName,productPrice;
    private CartAdapter cartAdapter;
    ProgressWheel progressWheel;
    RelativeLayout cartEmpty;
    Long id;
    CartActivity activity;
    private JSONArray cartListArray;
    private ArrayList<Product> cartArrayList = new ArrayList<>();
    ImageView addQuantity,removeQuantity;
    TextView itemQuantity,itemPrice,continueShop;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cartAdapter = new CartAdapter(getApplicationContext(),this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(cartAdapter);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Shopping Cart");

        initialize();
        onItemClick();
        sendJsonRequest();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(dialog!= null)
            dialog.dismiss();
    }

    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject cartList = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            cartList.put("getCartItems",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),cartList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                cartArrayList = parseJsonResponse(jsonObject);
                cartAdapter.setCartArrayList(cartArrayList);


                //console test
                Log.e("cartList", cartArrayList.toString());
                Log.e("input_payload",cartList.toString());
                Log.e("error", jsonObject.toString());
                Log.e("json",cartList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(CartActivity.this)
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
                + CART_ITEMS;

    }

    public ArrayList<Product> parseJsonResponse(JSONObject response) {
        if(progressWheel.getVisibility()== View.VISIBLE){
            progressWheel.setVisibility(View.GONE);
        }
        ArrayList<Product> cartArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_CART_LIST)) {
                    if(dataObject.isNull(KEY_CART_LIST)){
                        if(progressWheel.getVisibility()== View.VISIBLE){
                            progressWheel.setVisibility(View.GONE);
                        }
                        cartEmpty.setVisibility(View.VISIBLE);
                    }
                    else {
                        cartListArray = dataObject.getJSONArray(KEY_CART_LIST);
                        if (cartListArray != null)
                            for (int i = 0; i < cartListArray.length(); i++) {
                                JSONObject cartListObject = cartListArray.getJSONObject(i);
                                id = cartListObject.getLong(KEY_PRODUCT_ID);
                                productName = cartListObject.getString(KEY_PRODUCT_NAME);
                                productPrice = cartListObject.getString(KEY_PRODUCT_PRICE);
                                imageURL = cartListObject.getString(KEY_PRODUCT_IMAGE);


                                Product product = new Product();
                                product.setProduct_id(id);
                                product.setProduct_name(productName);
                                product.setImageURL(imageURL);
                                product.setPrice(productPrice);

                                cartArrayList.add(product);

                                Log.e("id", id.toString());
                                Log.e("image URL", imageURL);
                                Log.e("name", productName);
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
        return cartArrayList;
    }


    public void initialize(){
        addQuantity = (ImageView)findViewById(R.id.add_quantity);
        removeQuantity = (ImageView)findViewById(R.id.remove_quantity);
        itemQuantity = (TextView)findViewById(R.id.item_quantity);
        itemPrice = (TextView)findViewById(R.id.item_price);
        progressWheel = (ProgressWheel)findViewById(R.id.action_progress);
        preferenceClass = new SharedPreferenceClass(this);
        accessToken = preferenceClass.getAccessToken();
        cartEmpty = (RelativeLayout)findViewById(R.id.cart_empty);
        continueShop = (TextView)findViewById(R.id.continue_shopping);


    }

    public void onItemClick(){
        continueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void itemClicked(View view, int position) {

    }
}
