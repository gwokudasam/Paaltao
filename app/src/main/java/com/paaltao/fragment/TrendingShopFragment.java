package com.paaltao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paaltao.Adapters.TrendingShopAdapter;
import com.paaltao.R;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.logging.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arindam Dawn on 28-Jan-15.
 */
public class TrendingShopFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ProductListActivity activity;
    //SwipeRefreshLayout swipeRefreshLayout;
    private ImageView favorite;
    View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_trending_shop, container, false);
        initialize();
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeColors(R.color.primaryColor,R.color.greenMaterial,R.color.teal);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.trendingShop_recycler_view);
        mAdapter = new TrendingShopAdapter(getActivity(),getData(),activity);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }


    public static List<Product> getData(){
        ArrayList data = new ArrayList();
        int[] icons = {R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,
                R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small,R.drawable.apple_small};
        String[] product_names = {"Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone",
                "Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone","Automatic Drone"};
        String[] categories = {"Handmade","Photography","Electronics","Electronics","Electronics","Electronics","Electronics",
                "Electronics","Electronics","Electronics"};

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
//        swipeRefreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.swipe_to_refresh);
    }


    @Override
    public void onRefresh() {
        L.T(getActivity(),"Swipe to refresh called");
    }
}