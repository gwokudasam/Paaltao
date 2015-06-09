package com.paaltao.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.paaltao.Adapters.ProductStatusAdapter;
import com.paaltao.Adapters.TrendingShopAdapter;
import com.paaltao.R;
import com.paaltao.classes.Product;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.paaltao.extras.Keys.ProductList.KEY_CART_LIST;
import static com.paaltao.extras.Keys.ProductList.KEY_CATEGORY_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_PRICE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_STATUS;
import static com.paaltao.extras.Keys.ProductList.KEY_SELLER_PRODUCTS;
import static com.paaltao.extras.Keys.ProductList.KEY_UPLOADED_DATE;
import static com.paaltao.extras.urlEndPoints.CART_ITEMS;
import static com.paaltao.extras.urlEndPoints.GET_SELLER_PRODUCTS;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class ProductStatus extends AppCompatActivity implements ProductStatusAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    ProductStatusAdapter productStatusAdapter;
    SharedPreferenceClass preferenceClass;
    Long id;
    private JSONArray productStatusListArray;
    String accessToken,sellerId,productName,productPrice,uploadedDate,productStatus;
    private ArrayList<Product> productStatusArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_status);
        initialize();
        onClick();
        sendJsonRequest();
        mRecyclerView = (RecyclerView) findViewById(R.id.product_status_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productStatusAdapter = new ProductStatusAdapter(getApplicationContext(),this);
        mRecyclerView.setAdapter(productStatusAdapter);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("View product status");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(View view, int position) {

    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        accessToken = preferenceClass.getAccessToken();
        sellerId = preferenceClass.getSellerId();
    }
    public void onClick(){

    }

    public void sendJsonRequest(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject sellerProducts = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("vendorId",sellerId);
            sellerProducts.put("getSellersProducts",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),sellerProducts, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                productStatusArrayList = parseJsonResponse(jsonObject);
                productStatusAdapter.setProductStatusArrayList(productStatusArrayList);


                //console test
                Log.e("cartList", productStatusArrayList.toString());
                Log.e("input_payload",sellerProducts.toString());
                Log.e("error", jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(ProductStatus.this)
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
                + GET_SELLER_PRODUCTS;

    }

    public ArrayList<Product> parseJsonResponse(JSONObject response) {

        ArrayList<Product> productStatusArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_SELLER_PRODUCTS)) {
                    if(dataObject.isNull(KEY_SELLER_PRODUCTS)){

                    }
                    else {
                       productStatusListArray  = dataObject.getJSONArray(KEY_SELLER_PRODUCTS);
                        if (productStatusListArray != null)
                            for (int i = 0; i < productStatusListArray.length(); i++) {
                                JSONObject productStatusObject = productStatusListArray.getJSONObject(i);
                                id = productStatusObject.getLong(KEY_PRODUCT_ID);
                                productName = productStatusObject.getString(KEY_CATEGORY_NAME);
                                productPrice = productStatusObject.getString(KEY_PRODUCT_PRICE);
                                uploadedDate = productStatusObject.getString(KEY_UPLOADED_DATE);
                                productStatus = productStatusObject.getString(KEY_PRODUCT_STATUS);


                                Product product = new Product();
                                product.setProduct_id(id);
                                product.setProduct_name(productName);
                                product.setPrice(productPrice);
                                product.setStatus(productStatus);
                                product.setUploadedDate(uploadedDate);


                                productStatusArrayList.add(product);

                                Log.e("id", id.toString());
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
        return productStatusArrayList;
    }


}
