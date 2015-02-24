package com.paaltao.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paaltao.Adapters.CategoryAdapter;
import com.paaltao.R;
import com.paaltao.classes.Category;
import com.paaltao.classes.SharedPreferenceClass;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mRecyclerView = (RecyclerView) findViewById(R.id.category_grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        mRecyclerView.setAdapter(new CategoryAdapter(this,getData()));

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("select a category");






    }

    public static List<Category> getData(){
        ArrayList data = new ArrayList();
        int[] icons = {R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,
                R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small};

        String[] categories = {"Handmade","Photography","Electronics","Electronics","Electronics","Electronics","Electronics",
                "Electronics","Electronics","Electronics"};

        for(int i=0;  i<icons.length && i<categories.length;i++){
            Category current = new Category();
            current.setCategory_name(categories[i]);
            current.setCategory_id(icons[i]);
            data.add(current);
        }

        return data;
    }



}
