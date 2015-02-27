package com.paaltao.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paaltao.Adapters.FeaturedProductAdapter;
import com.paaltao.R;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.classes.SharedPreferenceClass;
import com.paaltao.logging.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arindam Dawn on 28-Jan-15.
 */
public class FragmentFeaturedProduct extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ProductListActivity activity;
    private ImageView favorite;
    View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.featured_product_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.featured_recycler_view);
        mAdapter = new FeaturedProductAdapter(getActivity(),getData(),activity);
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



}