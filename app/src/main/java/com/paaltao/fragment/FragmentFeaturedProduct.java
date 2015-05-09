package com.paaltao.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.*;
import static com.paaltao.extras.Keys.ProductList.*;
import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.FEATURED_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

/**
 * Created by Arindam Dawn on 28-Jan-15.
 */
public class FragmentFeaturedProduct extends Fragment {
    private RecyclerView mRecyclerView;
    private JSONArray featuredListArray;
    ProductListActivity activity;
    FeaturedProductAdapter featuredProductAdapter;
    private ImageView favorite;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    String accessToken = "67drd56g",productName,description,imageURL,price,isLiked;
    Long id;
    View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.featured_product_fragment, container, false);
        sendJsonRequest();

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.featured_recycler_view);
        featuredProductAdapter = new FeaturedProductAdapter(getActivity(), activity);
        mRecyclerView.setAdapter(featuredProductAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + FEATURED_LIST;

    }
    public void sendJsonRequest(){
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

                Log.e("url",UAT_BASE_URL+FEATURED_LIST);
                Log.e("error", jsonObject.toString());
                Log.e("json", getFeaturedList.toString());
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

                if (dataObject.has(KEY_FEATURED_LIST)) {
                    featuredListArray = dataObject.getJSONArray(KEY_FEATURED_LIST);
                    for (int i = 0; i < featuredListArray.length(); i++) {
                        JSONObject featuredProductObject = featuredListArray.getJSONObject(i);
                        id = featuredProductObject.getLong(KEY_PRODUCT_ID);
                        productName = featuredProductObject.getString(KEY_PRODUCT_NAME);
                        description = featuredProductObject.getString(KEY_PRODUCT_DESCRIPTION);
                        imageURL = featuredProductObject.getString(KEY_PRODUCT_IMAGE);
                        isLiked = featuredProductObject.getString(KEY_IS_LIKED);
                        price = featuredProductObject.getString(KEY_PRODUCT_PRICE);


                        Product product = new Product();
                        product.setProduct_id(id);
                        product.setProduct_name(productName);
                        product.setPrice(price);
                        product.setIs_liked(isLiked);
                        product.setImageURL(imageURL);

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