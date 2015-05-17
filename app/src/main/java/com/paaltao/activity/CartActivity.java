package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.paaltao.Adapters.CartAdapter;
import com.paaltao.R;
import com.paaltao.classes.Product;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ImageView addQuantity,removeQuantity;
    TextView itemQuantity,itemPrice;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new CartAdapter(getApplicationContext(),getData(),this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        Toolbar toolbar1 = (Toolbar)this.findViewById(R.id.toolbar_bottom);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar1.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar1.setTitle("Checkout");
        this.setSupportActionBar(toolbar);
        this.setTitle("Your Cart");

        initialize();
        onItemClick();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(dialog!= null)
            dialog.dismiss();
    }

    public static ArrayList getData(){
        ArrayList data = new ArrayList();
        int[] icons = {R.drawable.ic_launcher,R.drawable.ic_launcher};
        String[] product_names = {"Automatic Drone","Automatic Drone"};
        String[] categories = {"Handmade","Photography"};

        for(int i=0; i<product_names.length && i<icons.length && i<categories.length;i++){
            Product current = new Product();
            current.setProduct_name(product_names[i]);
            current.setProduct_category(categories[i]);
            current.setProduct_id(icons[i]);
            data.add(current);
        }

        return data;
    }

    public void initialize(){
        addQuantity = (ImageView)findViewById(R.id.add_quantity);
        removeQuantity = (ImageView)findViewById(R.id.remove_quantity);
        itemQuantity = (TextView)findViewById(R.id.item_quantity);
        itemPrice = (TextView)findViewById(R.id.item_price);


    }

    public void onItemClick(){
    }



}
