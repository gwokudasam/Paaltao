package com.paaltao.activity;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.paaltao.R;
import com.paaltao.classes.BadgeView;
import com.paaltao.classes.FloatingActionButton;
import com.paaltao.classes.FloatingActionsMenu;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.fragment.AccountFragment;
import com.paaltao.fragment.FragmentFeaturedProduct;
import com.paaltao.fragment.TrendingShopFragment;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;
import com.paaltao.persistentsearch.SearchBox;
import com.paaltao.persistentsearch.SearchResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

import static com.paaltao.extras.Keys.ProductList.KEY_CART_QUANTITY;
import static com.paaltao.extras.Keys.ProductList.KEY_DATA;
import static com.paaltao.extras.urlEndPoints.CART_QUANTITY;
import static com.paaltao.extras.urlEndPoints.CATEGORY_LIST;
import static com.paaltao.extras.urlEndPoints.UAT_BASE_URL;


public class HomeActivity extends AppCompatActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Resources res;
    SearchBox search;
    int cartQty;
    MenuItem badge_item_cart;
    BadgeView badge_cart;
    int badge_item_id_cart;
    View target_cart;
    Menu badge_menu;
    FloatingActionsMenu floatingActionsMenu;
    Toolbar toolbar;
    FloatingActionButton openShop,actionB;
    RelativeLayout overlay;
    String vendor_login,accessToken,badgeValue;
    SharedPreferenceClass preferenceClass;

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG","onRESUME");
        if (accessToken == null || accessToken.length() == 0){
            Intent intent = new Intent(HomeActivity.this,IntroPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        res = this.getResources();
        Log.e("TAG","onCreate");
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        // init toolbar (old action bar)
        accessToken = preferenceClass.getAccessToken();


        if (accessToken == null || accessToken.length() == 0){
            Intent intent = new Intent(HomeActivity.this,IntroPageActivity.class);
            startActivity(intent);
            finish();
        }
        //fetching cart quantity
        getCartNumber();



        floatingActionsMenu = new FloatingActionsMenu(getApplicationContext());

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
        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(actionC);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.blue500));


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

        badge_item_cart = menu.findItem(R.id.cart_icon);
        badge_item_id_cart = badge_item_cart.getItemId();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                target_cart = findViewById(badge_item_id_cart);
                badge_cart = new BadgeView(HomeActivity.this,
                        target_cart);
                badge_cart.setText(preferenceClass.getCartItem().toString());
                badge_cart.setTextColor(Color.parseColor("#ffffff"));
                badge_cart.setBadgeBackgroundColor(getResources().getColor(R.color.teal));


            }
        }, 1000);






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



    public void getCartNumber(){
        final JSONObject jsonObject = new JSONObject();
        final JSONObject cartQty = new JSONObject();
        try{
            jsonObject.put("accessToken",accessToken);
            cartQty.put("getCartQty",jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(),cartQty, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                parseJsonData(jsonObject);
                Log.e("json", cartQty.toString());
                L.m(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    new SnackBar.Builder(HomeActivity.this)
                            .withMessage("No Internet Connection!")
                            .withTextColorId(R.color.white)
                            .withDuration((short) 6000)
                            .show();

                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {

                    //TODO
                } else if (volleyError instanceof ParseError) {

                    //TODO
                }

            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.put("Cookie",preferenceClass.getCookie());
                return headers;
            }};
        requestQueue.add(jsonObjectRequest);
    }
    public static String getRequestUrl() {

        return UAT_BASE_URL
                + CART_QUANTITY;

    }


    public void parseJsonData(JSONObject jsonObject){

        try{
            JSONObject dataObject = jsonObject.getJSONObject(KEY_DATA);
            if (dataObject.has(KEY_CART_QUANTITY)){
                if (dataObject.isNull(KEY_CART_QUANTITY)){
                    return;
                }
                else{
                   cartQty = dataObject.getInt(KEY_CART_QUANTITY) ;
                    preferenceClass.saveCartItem(cartQty);

                }

            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }
    }
}