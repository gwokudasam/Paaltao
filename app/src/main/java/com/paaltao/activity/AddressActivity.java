package com.paaltao.activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paaltao.Adapters.AddressAdapter;
import com.paaltao.R;
import com.paaltao.classes.Address;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mRecyclerView = (RecyclerView)findViewById(R.id.address_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new AddressAdapter(getApplicationContext(),getData(),this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);



        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("My Address");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_address){
            Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArrayList getData(){
        ArrayList data = new ArrayList();
        String[] title = {"My Home","My Workplace","Girlfriend's House","Uncle's Place"};
        String[] street_no = {"27/D","1/18","47F","89C"};
        String[] street_name = {"Kingston Road,Wadala","Poddar Nagar,Jadavpur","Elgin Road,Rabindra Sadan","Camac Street"};
        String[] landmark = {"Near Kingston School","South City Mall","Widlife Park","Audi Showroom"};
        String[] pincode = {"718767","700068","765678","786765"};
        String[] city = {"Mumbai","Kolkata","Allahabad","Bangalore"};
        String[] state = {"Maharastra","West Bengal","Gujrat","Karnataka"};
        String[] country = {"India","India","India","India"};

        for (int i = 0;i<title.length &&i<pincode.length && i<city.length && i<state.length;i++){
            Address current = new Address();
            current.setTitle(title[i]);
            current.setStreetNo(street_no[i]);
            current.setStreetName(street_name[i]);
            current.setLandmark(landmark[i]);
            current.setPincode(pincode[i]);
            current.setCity(city[i]);
            current.setState(state[i]);
            current.setCountry(country[i]);
            data.add(current);

        }
        return data;
    }


}
