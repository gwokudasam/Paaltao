package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.paaltao.R;

public class ProfileActivity extends ActionBarActivity {
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Profile");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        initialize();
        onItemClick();
    }

    public void initialize(){
        address = (TextView)findViewById(R.id.my_address);
    }

    public void onItemClick(){
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,AddressActivity.class);
                startActivity(intent);
            }
        });
    }


}
