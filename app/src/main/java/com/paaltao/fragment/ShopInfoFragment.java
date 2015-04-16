package com.paaltao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.paaltao.R;
import com.paaltao.activity.AddressActivity;
import com.paaltao.activity.ProfileActivity;

//This fragment contains shop Shipping Info
public class ShopInfoFragment extends Fragment {
    RelativeLayout accountLink,my_address;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        initialize();
        onItemClick();
        return view;
    }
    public void initialize(){

    }

    public void onItemClick(){

    }

}




