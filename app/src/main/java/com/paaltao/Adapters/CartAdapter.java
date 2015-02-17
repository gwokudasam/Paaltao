package com.paaltao.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.classes.Product;

import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    public Context context;
    private View view;
    private LayoutInflater inflater;
    List<Product> data = Collections.emptyList();
    private RecyclerView recyclerView;
    private Activity contextActivity;




    public CartAdapter(Context context ,List<Product> data, Activity contextActivity){
        this.data = data;
        this.context= context;
        this.contextActivity = contextActivity;
    }
    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.cart_row,parent,false);


        CartHolder holder = new CartHolder(view);



        return holder;
    }


    @Override
    public void onBindViewHolder(CartHolder holder, int position) {

        final Product current = data.get(position);
        holder.productName.setText(current.getProduct_name());
        holder.productImage.setImageResource(current.getProduct_id());

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(current);
                Log.d("Tag Name", "Log Message"+getItemCount());

                if(getItemCount()==0){
                    Log.d("Tag Name", "No more items left");
                     SweetAlertDialog dialog = new SweetAlertDialog(contextActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                            dialog.setTitleText("oops!");
                            dialog.setContentText("Your Cart seems to be a quite lonely place!");
                            dialog.setCustomImage(R.drawable.ic_launcher);
                            dialog.setConfirmText("OK");
                            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    contextActivity.finish();
                                }
                            });


                            dialog.show();


                }
                }



        });



    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void remove(Product item) {
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }



    class CartHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView categoryName;
        ImageView productImage;
        TextView removeItem;

        public CartHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.product_image_thumbnail);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            removeItem = (TextView) itemView.findViewById(R.id.remove_item);
        }
    }
}