package com.paaltao.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.paaltao.Adapters.CategoryAdapter;
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
import java.util.List;

import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.Keys.ProductList.KEY_ERROR_CODE;
import static com.paaltao.extras.Keys.UserCredentials.*;
import static com.paaltao.extras.Keys.ProductList.*;
import static com.paaltao.extras.urlEndPoints.BASE_URL;
import static com.paaltao.extras.urlEndPoints.CATEGORY_LIST;
import static com.paaltao.extras.urlEndPoints.FEATURED_LIST;
import static com.paaltao.extras.urlEndPoints.PRODUCT_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.ClickListener{

    private RecyclerView mRecyclerView;
    Long id;
    private JSONArray categoryListArray;
    String accessToken = "67drd56g",imageURL,categoryName;
    CategoryAdapter categoryAdapter;
    CategoryActivity activity;
    ProgressWheel progressWheel;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initialize();
        sendJsonRequest();
        mRecyclerView = (RecyclerView) findViewById(R.id.category_grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        categoryAdapter = new CategoryAdapter(activity,getApplicationContext());
        mRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setClickListener(this);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Categories");

    }

    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject categoryList = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            categoryList.put("categoryList",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),categoryList, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                categoryArrayList = parseJsonResponse(jsonObject);
                categoryAdapter.setCategoryArrayList(categoryArrayList);


                //console test
                Log.e("categoryList",categoryArrayList.toString());
                Log.e("url", UAT_BASE_URL + CATEGORY_LIST);
                Log.e("input_payload",categoryList.toString());
                Log.e("error", jsonObject.toString());
                Log.e("json", categoryList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(CategoryActivity.this)
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
                + CATEGORY_LIST;

    }
    public void initialize(){
        progressWheel = (ProgressWheel)findViewById(R.id.action_progress);
    }

    public ArrayList<Category> parseJsonResponse(JSONObject response) {
        if(progressWheel.getVisibility()== View.VISIBLE){
            progressWheel.setVisibility(View.GONE);
        }
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                JSONObject dataObject = response.getJSONObject(KEY_DATA);

                if (dataObject.has(KEY_CATEGORY_LIST)) {
                    categoryListArray = dataObject.getJSONArray(KEY_CATEGORY_LIST);
                    for (int i = 0; i < categoryListArray.length(); i++) {
                        JSONObject categoryListObject = categoryListArray.getJSONObject(i);
                        id = categoryListObject.getLong(KEY_CATEGORY_ID);
                        categoryName = categoryListObject.getString(KEY_CATEGORY_NAME);
                        imageURL = categoryListObject.getString(KEY_CATEGORY_IMAGE);


                        Category category = new Category();
                        category.setCategory_id(id);
                        category.setCategory_name(categoryName);
                        category.setImageURL(imageURL);

                        categoryArrayList.add(category);

                        Log.e("id",id.toString());
                        Log.e("image URL", imageURL);
                        Log.e("name",categoryName);

                    }
                }
                if (response.has(KEY_ERROR_CODE)) {
                    JSONObject errorObject = response.getJSONObject(KEY_ERROR_CODE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryArrayList;
    }


    @Override
    public void itemClicked(View view, int position) {
        Long cat_id = categoryArrayList.get(position).getCategory_id();
        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
        intent.putExtra("catId", cat_id.toString());
        startActivity(intent);


    }
}
