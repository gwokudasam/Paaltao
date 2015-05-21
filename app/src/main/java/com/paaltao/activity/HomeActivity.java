package com.paaltao.activity;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.paaltao.R;
import com.paaltao.classes.FloatingActionButton;
import com.paaltao.classes.FloatingActionsMenu;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.fragment.AccountFragment;
import com.paaltao.fragment.FragmentFeaturedProduct;
import com.paaltao.fragment.TrendingShopFragment;
import com.paaltao.persistentsearch.SearchBox;
import com.paaltao.persistentsearch.SearchResult;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class HomeActivity extends AppCompatActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Resources res;
    SearchBox search;
    Toolbar toolbar;
    FloatingActionButton openShop,actionB;
    RelativeLayout overlay;
    String vendor_login;
    SharedPreferenceClass preferenceClass;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        res = this.getResources();
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        // init toolbar (old action bar)

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);

        overlay = (RelativeLayout)findViewById(R.id.white_opacity);

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });

        toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_launcher);
        this.setSupportActionBar(toolbar);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return true;
//            }
//        });

//        floating action menu
        //final View actionB = findViewById(R.id.action_b);

        //  Floating Action Bar Actions
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Categories");


        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
            }


        });
        actionC.setIcon(R.drawable.ic_check_white);
        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(actionC);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));


        actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setTitle("Manage shop");
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ManageShopActivity.class);
                startActivity(intent);
            }
        });

        openShop = (FloatingActionButton)findViewById(R.id.open_shop_fab);
        openShop.setTitle("Open a shop");

        if(preferenceClass.getVendorLoginSuccess() != null){
            vendor_login = preferenceClass.getVendorLoginSuccess();}

        if(vendor_login != null){
            Log.i("vendor_login",vendor_login);
            if(vendor_login.contains("true")){

                openShop.setVisibility(openShop.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
            else {
                Log.e("vendor_login",vendor_login);
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }

        }
        openShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,OpenShopActivity.class);
                startActivity(intent);
            }
        });

        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
                pager.setCurrentItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(getPageTitle(i))
                            .setTabListener(this)
            );
        }

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            switch (index) {
                case 0:
                    return new FragmentFeaturedProduct();
                case 1:
                    return new TrendingShopFragment();
                case 2:
                    return new AccountFragment();


            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "tab";
        }
    }

    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private String getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Featured";
            case 1:
                return "Trending";
            case 2:
                return "Account";


        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_icon) {
            Intent intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_search){
            openSearch();
        }


        return super.onOptionsItemSelected(item);
    }

    //openSearch

    public void openSearch() {
        overlay.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);
        for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result "
                    + Integer.toString(x), getResources().getDrawable(
                    R.drawable.ic_history));
            search.addSearchable(option);
        }
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(HomeActivity.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();

            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(HomeActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);

            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        overlay.setVisibility(View.GONE);
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())toolbar.setTitle("");

    }



}