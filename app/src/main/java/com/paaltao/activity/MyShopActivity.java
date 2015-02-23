package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.paaltao.Adapters.ShopProductAdapter;
import com.paaltao.R;
import com.paaltao.classes.AddFloatingActionButton;
import com.paaltao.classes.Product;

import java.util.ArrayList;
import java.util.List;

public class MyShopActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        mRecyclerView = (RecyclerView) findViewById(R.id.shop_products_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(new ShopProductAdapter(getApplicationContext(),getData()));

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Shop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddFloatingActionButton addProductButton = (AddFloatingActionButton)findViewById(R.id.multiple_actions1);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag","Clicked");
                Intent intent = new Intent(MyShopActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

    }

    public static List<Product> getData(){
        ArrayList data = new ArrayList();
        int[] icons = {R.drawable.apple_small,R.drawable.ic_launcher,R.drawable.bag_icon,R.drawable.notify_icon,R.drawable.apple_small,
                R.drawable.bag_icon,R.drawable.apple_small,R.drawable.notify_icon,R.drawable.apple_small,R.drawable.ic_launcher};

        String[] product_name = {"Handmade","Photography","Electronics","Electronics","Electronics","Electronics","Electronics",
                "Electronics","Electronics","Electronics"};

        for(int i=0;  i<icons.length && i< product_name.length;i++){
            Product current = new Product();
            current.setProduct_name(product_name[i]);
            current.setProduct_id(icons[i]);
            data.add(current);
        }

        return data;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_shop, menu);
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
