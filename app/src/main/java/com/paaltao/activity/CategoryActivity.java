package com.paaltao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paaltao.Controller.CategoryAdapter;
import com.paaltao.R;
import com.paaltao.classes.Category;

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
        mRecyclerView.setAdapter(new CategoryAdapter(getApplicationContext(),getData()));

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);





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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
}
