package com.paaltao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.paaltao.R;
import com.paaltao.classes.BadgeView;

import java.util.HashMap;

public class ProductDetailsActivity extends ActionBarActivity implements BaseSliderView.OnSliderClickListener{

    TextView itemDetailsContent;
    TextView itemDetailsTitle;
    Menu badge_menu;
    MenuItem badge_item_cart;
    int badge_item_id_cart;
    View target_cart;
    BadgeView badge_cart;
    Button cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        SliderLayout mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);


        HashMap<String,String> url_maps = new HashMap<>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal",R.drawable.apple_small);
//        file_maps.put("Big Bang Theory",R.drawable.apple_small);
//        file_maps.put("House of Cards",R.drawable.apple_small);
//        file_maps.put("Game of Thrones", R.drawable.apple_small);

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(4000);

        initialize();
        onItemClick();



       
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_details, menu);



        badge_menu = menu;

        badge_item_cart = menu.findItem(R.id.cart_icon);
        badge_item_id_cart = badge_item_cart.getItemId();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                target_cart = findViewById(badge_item_id_cart);
                badge_cart = new BadgeView(ProductDetailsActivity.this,
                        target_cart);
                badge_cart.setText("1");
                badge_cart
                        .setTextColor(Color.parseColor("#ffffff"));


            }
        }, 1000);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_icon) {
            Intent intent = new Intent(ProductDetailsActivity.this,CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize() // Method to initialize all variables
    {
        itemDetailsTitle = (TextView)findViewById(R.id.item_details_title);
        itemDetailsContent = (TextView)findViewById(R.id.item_details_content);
        cartButton = (Button)findViewById(R.id.add_cart_btn);

    }

    public void onItemClick()
    {
        itemDetailsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemDetailsContent.getVisibility()== View.GONE) {
                    itemDetailsContent.setVisibility(View.VISIBLE);
                }
                else{
                    itemDetailsContent.setVisibility(View.GONE);
                }

            }

        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badge_cart.show();
            }
        });


    }

}
