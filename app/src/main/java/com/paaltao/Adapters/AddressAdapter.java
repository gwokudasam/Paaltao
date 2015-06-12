package com.paaltao.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.activity.AddAddressActivity;
import com.paaltao.activity.AddressActivity;
import com.paaltao.classes.Address;
import com.paaltao.classes.Product;
import com.paaltao.network.VolleySingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Address Adapter which has been created to bind the various address fields to the address packet in the recycler view.
 * Created by Arindam Dawn.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
    private View view;
    private Context context;
    private VolleySingleton singleton;
    private LayoutInflater inflater;
    private ClickListener clickListener;
    private AddressActivity contextActivity;
    private ArrayList<Address> addressArrayList = new ArrayList<>();

    public AddressAdapter(Context context, AddressActivity contextActivity){
        this.context = context;
        singleton = VolleySingleton.getsInstance();
        this.contextActivity =  contextActivity;
    }
    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.address_row,parent,false);
        AddressHolder holder = new AddressHolder(view);
        return holder;
    }

    public void setAddressArrayList(ArrayList<Address> addressArrayList){
        this.addressArrayList = addressArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, addressArrayList.size());
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {

        final Address current = addressArrayList.get(position);
        holder.firstName.setText(current.getFirstName());
        holder.lastName.setText(current.getLastName());
        holder.street_name.setText(current.getStreetName());
        holder.company.setText(current.getCompany());
        holder.city.setText(current.getCity());
        holder.state.setText(current.getState());
        holder.country.setText(current.getCountry());
        holder.pincode.setText(current.getPincode());
        holder.contact.setText(current.getContact());

//        holder.remove_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                remove(current);
//
//                if(getItemCount() == 0)
//                {
//                    Intent intent = new Intent(contextActivity, AddAddressActivity.class);
//                    contextActivity.startActivity(intent);
//                }
//            }
//        });

    }

    public void remove(Address item) {
        int position = addressArrayList.indexOf(item);
        addressArrayList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }



    class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView firstName,lastName,company,street_name,city,state,country,remove_address,edit_address,pincode,contact;

        public AddressHolder(View itemView) {
            super(itemView);

            firstName = (TextView)itemView.findViewById(R.id.firstName);
            lastName = (TextView)itemView.findViewById(R.id.lastName);
            company = (TextView)itemView.findViewById(R.id.companyName);
            street_name = (TextView)itemView.findViewById(R.id.street_name);
            city = (TextView)itemView.findViewById(R.id.city_name);
            state = (TextView)itemView.findViewById(R.id.state);
            country = (TextView)itemView.findViewById(R.id.country);
            pincode = (TextView)itemView.findViewById(R.id.pincode);
            contact = (TextView)itemView.findViewById(R.id.contact);
            remove_address = (TextView)itemView.findViewById(R.id.remove_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }
    public interface ClickListener{
        void itemClicked(View view, int position);

    }
}
