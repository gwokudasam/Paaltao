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
import com.paaltao.activity.HomeActivity;
import com.paaltao.activity.ProfileActivity;

//This is a user account fragment.
public class AccountFragment extends Fragment {
    RelativeLayout accountLink,my_address;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initialize();
        onItemClick();
        return view;
    }
    public void initialize(){
        accountLink = (RelativeLayout)view.findViewById(R.id.account_link);
        my_address = (RelativeLayout)view.findViewById(R.id.my_address);
    }

    public void onItemClick(){
        accountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
        my_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });
    }

    }




