package com.paaltao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.paaltao.activity.ProductDetailsActivity;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.*;
import static com.paaltao.extras.Keys.UserCredentials.KEY_VENDOR_ID;
import static com.paaltao.extras.urlEndPoints.FEATURED_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

/**
 * Created by Arindam Dawn on 28-Jan-15.
 */
public class FragmentFeaturedProduct extends Fragment implements FeaturedProductAdapter.ClickListener{
    private RecyclerView mRecyclerView;
    private JSONArray featuredListArray;
    ProductListActivity activity;
    RelativeLayout noInternet;
    FeaturedProductAdapter featuredProductAdapter;
    private ImageView favorite;
    SharedPreferenceClass preferenceClass;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    String accessToken,productName,imageURL,price,isLiked,shopName,description,vendorId;
    Long id;
    ProgressWheel progressWheel;
    View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.featured_product_fragment, container, false);
        progressWheel = (ProgressWheel)layout.findViewById(R.id.action_progress_featured);
        preferenceClass = new SharedPreferenceClass(getActivity());
        noInternet = (RelativeLayout)getActivity().findViewById(R.id.no_internet);
        accessToken = preferenceClass.getAccessToken();
        sendJsonRequest();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.featured_recycler_view);
        featuredProductAdapter = new FeaturedProductAdapter(getActivity(), activity);
        mRecyclerView.setAdapter(featuredProductAdapter);
        featuredProductAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + FEATURED_LIST;

    }
    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject getFeaturedList = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            getFeaturedList.put("getFeaturedList",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),getFeaturedList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                productArrayList = parseJsonResponse(jsonObject);
                featuredProductAdapter.setProductArrayList(productArrayList);
                Log.e("productArray",productArrayList.toString());

//                Log.e("url",UAT_BASE_URL+FEATURED_LIST);
//                Log.e("error", jsonObject.toString());
//                Log.e("json", getFeaturedList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        new SnackBar.Builder(getActivity())
                                .withMessage("No Internet Connection!")
                                .withTextColorId(R.color.white)
                                .withDuration((short) 6000)
                                .show();
                        noInternet.setVisibility(View.VISIBLE);

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
        if(progressWheel.getVisibility()== View.VISIBLE){
            progressWheel.setVisibility(View.GONE);
        }
        ArrayList<Product> productArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_FEATURED_LIST)) {
                    featuredListArray = dataObject.getJSONArray(KEY_FEATURED_LIST);
                    for (int i = 0; i < featuredListArray.length(); i++) {
                        JSONObject featuredProductObject = featuredListArray.getJSONObject(i);
                        id = featuredProductObject.getLong(KEY_PRODUCT_ID);
                        productName = featuredProductObject.getString(KEY_PRODUCT_NAME);
                        shopName = featuredProductObject.getString(KEY_SHOP_NAME);
                        description = featuredProductObject.getString(KEY_PRODUCT_DESCRIPTION);
                        vendorId = featuredProductObject.getString(KEY_VENDOR_ID);

                        imageURL = featuredProductObject.getString(KEY_PRODUCT_IMAGE);
                        isLiked = featuredProductObject.getString(KEY_IS_LIKED);
                        price = featuredProductObject.getString(KEY_PRODUCT_PRICE);


                        Product product = new Product();
                        product.setProduct_id(id);
                        product.setProduct_name(productName);
                        product.setPrice(price);
                        product.setShop_name(shopName);
                        product.setIs_liked(isLiked);
                        product.setDescription(description);
                        product.setImageURL(imageURL);
                        product.setVendorId(vendorId);

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


    @Override
    public void onResume() {
        sendJsonRequest();
        super.onResume();
    }

    @Override
    public void itemClicked(View view, int position) {
        Long product_id = productArrayList.get(position).getProduct_id();
        String productDetails = productArrayList.get(position).getDescription();
        String name = productArrayList.get(position).getProduct_name();
        String productPrice = productArrayList.get(position).getPrice();
        String shop = productArrayList.get(position).getShop_name();
        String vendorId = productArrayList.get(position).getVendorId();
        Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
        intent.putExtra("productId", product_id.toString());
        intent.putExtra("description",productDetails);
        intent.putExtra("productName",name);
        intent.putExtra("productPrice",productPrice);
        intent.putExtra("shopName",shop);
        intent.putExtra("vendorId",vendorId);
        startActivity(intent);
    }
}