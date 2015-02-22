package com.paaltao.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.activity.AddressActivity;
import com.paaltao.classes.Address;

import java.util.Collections;
import java.util.List;

/**
 * Address Adapter which has been created to bind the various address fields to the address packet in the recycler view.
 * Created by Arindam Dawn.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
    private View view;
    private Context context;
    private LayoutInflater inflater;
    private Activity contextActivity;
    List<Address> data = Collections.emptyList();

    public AddressAdapter(Context context, List<Address> data, Activity contextActivity){
        this.context = context;
        this.data = data;
        this.contextActivity = contextActivity;
    }
    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.address_row,parent,false);
        AddressHolder holder = new AddressHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {

        final Address current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.street_no.setText(current.getStreetNo());
        holder.street_name.setText(current.getStreetName());
        holder.landmark.setText(current.getLandmark());
        holder.city.setText(current.getCity());
        holder.state.setText(current.getState());
        holder.country.setText(current.getCountry());
        holder.pincode.setText(current.getPincode());

        holder.remove_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(current);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void remove(Address item) {
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }

    class AddressHolder extends RecyclerView.ViewHolder{
        TextView title,street_no,street_name,landmark,city,state,country,remove_address,edit_address,pincode;


        public AddressHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.address_title);
            street_no = (TextView)itemView.findViewById(R.id.street_no);
            street_name = (TextView)itemView.findViewById(R.id.street_name);
            landmark = (TextView)itemView.findViewById(R.id.landmark_name);
            city = (TextView)itemView.findViewById(R.id.city_name);
            state = (TextView)itemView.findViewById(R.id.state);
            country = (TextView)itemView.findViewById(R.id.country);
            remove_address = (TextView)itemView.findViewById(R.id.remove_address);
            pincode = (TextView)itemView.findViewById(R.id.pincode);
            edit_address = (TextView)itemView.findViewById(R.id.edit_address);

        }
    }
}
