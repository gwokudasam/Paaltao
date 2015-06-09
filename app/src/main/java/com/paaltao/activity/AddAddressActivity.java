package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.paaltao.R;
import com.paaltao.classes.SharedPreferenceClass;

public class AddAddressActivity extends AppCompatActivity {
    EditText firstName_field,lastName_field;
    SharedPreferenceClass preferenceClass;
    String firstName,lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initialize();

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        this.setTitle("Add Address");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        firstName_field = (EditText)findViewById(R.id.firstName_field);
        lastName_field = (EditText)findViewById(R.id.lastName_field);
        firstName = preferenceClass.getFirstName();
        lastName = preferenceClass.getLastName();
        firstName_field.setText(firstName);
        lastName_field.setText(lastName);
    }
}
