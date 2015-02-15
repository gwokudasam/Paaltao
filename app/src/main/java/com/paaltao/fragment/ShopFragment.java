package com.paaltao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.paaltao.R;
import com.paaltao.activity.OpenShopActivity;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class ShopFragment extends Fragment {
    View view;
    Context context;
    Button openShopBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.open_shop_fragment,container,false);

        initialize();
        onClickItem();
        return view;


    }
    public void initialize(){
        openShopBtn = (Button)view.findViewById(R.id.open_shop_button);

    }
    public void onClickItem(){
        openShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), OpenShopActivity.class));
            }
        });
    }
}
