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
import com.paaltao.Adapters.TrendingShopAdapter;
import com.paaltao.R;
import com.paaltao.activity.MyShopActivity;
import com.paaltao.classes.Product;
import com.paaltao.classes.ProgressWheel;
import com.paaltao.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.ProductList.KEY_PRODUCT_ID;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_IMAGE;
import static com.paaltao.extras.Keys.ProductList.KEY_SHOP_NAME;
import static com.paaltao.extras.Keys.ProductList.KEY_TRENDING_SHOP_LIST;
import static com.paaltao.extras.urlEndPoints.TRENDING_SHOP_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

/**
 * Created by Arindam Dawn on 28-Jan-15.
 */
public class TrendingShopFragment extends Fragment implements TrendingShopAdapter.ClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    TrendingShopAdapter trendingShopAdapter;
    MyShopActivity activity;
    private ImageView favorite;
    private ArrayList<Product> trendingShopList = new ArrayList<>();
    String accessToken = "67drd56g",shopName,shopImageUrl;
    Long id;
    ProgressWheel progressWheel;
    private JSONArray trendingShopListArray;
    View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_trending_shop, container, false);
        progressWheel = (ProgressWheel)layout.findViewById(R.id.action_progress_trending);
        sendJsonRequest();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.trendingShop_recycler_view);
        trendingShopAdapter = new TrendingShopAdapter(getActivity(),activity);
        mRecyclerView.setAdapter(trendingShopAdapter);
        trendingShopAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static String getRequestUrl() {

        return UAT_BASE_URL
                + TRENDING_SHOP_LIST;

    }

    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject getTrendingShopList = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            getTrendingShopList.put("getTrendingShopList",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),getTrendingShopList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                  trendingShopList = parseJsonResponse(jsonObject);
                  trendingShopAdapter.setShopArrayList(trendingShopList);
                  Log.e("productArray", trendingShopList.toString());

//                Log.e("url",UAT_BASE_URL+TRENDING_SHOP_LIST);
//                Log.e("error", jsonObject.toString());
//                Log.e("json", trendingShopList.toString());
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
        if(progressWheel.getVisibility()== View.VISIBLE){
            progressWheel.setVisibility(View.GONE);
        }
        ArrayList<Product> shopArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_TRENDING_SHOP_LIST)) {
                    trendingShopListArray = dataObject.getJSONArray(KEY_TRENDING_SHOP_LIST);
                    for (int i = 0; i < trendingShopListArray.length(); i++) {
                        JSONObject trendingShopObject = trendingShopListArray.getJSONObject(i);
                        id = trendingShopObject.getLong(KEY_PRODUCT_ID);
                        shopName = trendingShopObject.getString(KEY_SHOP_NAME);

                        shopImageUrl = trendingShopObject.getString(KEY_SHOP_IMAGE);


                        Product product = new Product();
                        product.setProduct_id(id);
                        product.setShop_name(shopName);
                        product.setShop_image_url(shopImageUrl);

                        shopArrayList.add(product);

                    }
                }
                if (response.has(KEY_ERROR_CODE)) {
                    JSONObject errorObject = response.getJSONObject(KEY_ERROR_CODE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return shopArrayList;
    }


    @Override
    public void itemClicked(View view, int position) {

        startActivity(new Intent(getActivity(), MyShopActivity.class));
    }


}