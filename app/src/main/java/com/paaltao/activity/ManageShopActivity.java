package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.paaltao.R;

public class ManageShopActivity extends AppCompatActivity {
    RelativeLayout layout,layout1,layout2,layout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setTitle("Manage Shop");
        this.setSupportActionBar(toolbar);
        initialize();
        onClick();
    }


    public void initialize(){
       layout = (RelativeLayout)findViewById(R.id.visit_my_shop);
       layout1 = (RelativeLayout)findViewById(R.id.edit_shop);
       layout2 = (RelativeLayout)findViewById(R.id.shop_orders);
       layout3 = (RelativeLayout)findViewById(R.id.add_product);
    }

    public void onClick(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageShopActivity.this,ShopActivity.class);
                startActivity(intent);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageShopActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageShopActivity.this,OpenShopActivity.class);
                startActivity(intent);
                intent.putExtra("editShop",true);
            }
        });

    }



}
