package com.paaltao.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.paaltao.Adapters.FeaturedProductAdapter;
import com.paaltao.R;
import com.paaltao.classes.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ProductListActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mRecyclerView = (RecyclerView)findViewById(R.id.product_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FeaturedProductAdapter(this,activity);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
    }

    public static List<Product> getData(){
        ArrayList data = new ArrayList();
        int[] icons = {R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,
                R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small};
        String[] product_names = {"Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone",
                "Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone"};
        String[] categories = {"Handmade","Photography","Electronics","Electronics","Electronics","Electronics","Electronics",
                "Electronics","Electronics","Electronics"};

        for(int i=0; i<product_names.length && i<icons.length && i<categories.length;i++){
            Product current = new Product();
            current.setProduct_name(product_names[i]);
            current.setProduct_category(categories[i]);
            current.setProduct_id(icons[i]);
            data.add(current);
        }

        return data;
    }


}
