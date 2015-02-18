package com.paaltao.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.paaltao.R;
import com.paaltao.classes.AddFloatingActionButton;
import com.paaltao.classes.FloatingActionButton;
import com.paaltao.classes.FloatingActionsMenu;
import com.paaltao.fragment.AccountFragment;
import com.paaltao.fragment.FragmentFeaturedProduct;
import com.paaltao.fragment.ShopFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class HomeActivity extends ActionBarActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        res = this.getResources();
        // init toolbar (old action bar)

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setTitle("you are awesome!");
        this.setSupportActionBar(toolbar);

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
        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(actionC);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));


        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setTitle("Shop");
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyShopActivity.class);
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
                            .setIcon(getIcon(i))
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
                    return new ShopFragment();
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
    private Drawable getIcon(int position) {
        switch (position) {
            case 0:
                return res.getDrawable(R.drawable.ic_notification);
            case 1:
                return res.getDrawable(R.drawable.ic_cart);
            case 2:
                return res.getDrawable(R.drawable.ic_cart);


        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_product_details, menu);
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

        return super.onOptionsItemSelected(item);
    }



}