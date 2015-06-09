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

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.paaltao.R;
import com.paaltao.classes.Category;
import com.paaltao.classes.Product;
import com.paaltao.network.VolleySingleton;

import java.util.ArrayList;
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
    private ImageLoader imageLoader;
    private VolleySingleton singleton;
    private ClickListener clickListener;
    private ArrayList<Product> cartArrayList = new ArrayList<>();
    private Activity contextActivity;

    public CartAdapter(Context context , Activity contextActivity){
        this.context= context;
        this.contextActivity = contextActivity;
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
    }
    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.cart_row,parent,false);

        CartHolder holder = new CartHolder(view);
        return holder;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    public void setCartArrayList(ArrayList<Product> cartArrayList){
        this.cartArrayList = cartArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, cartArrayList.size());
    }



    @Override
    public void onBindViewHolder(final CartHolder holder, int position) {

        final Product current = cartArrayList.get(position);
        holder.productName.setText(current.getProduct_name());
        holder.productPrice.setText(current.getPrice());

        final String imageURL = current.getImageURL();

        if(imageURL != null){
            imageLoader.get(imageURL, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    holder.productImage.setImageBitmap(imageContainer.getBitmap());
                    Log.e("imageURLAdapter",imageURL);

                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("imageURL","no image found");

                }
            });
        }






    }


    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }


    public void remove(Product item) {
        int position = cartArrayList.indexOf(item);
        cartArrayList.remove(position);
        notifyItemRemoved(position);
    }



    class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView productName,productPrice;
        ImageView productImage;
        TextView removeItem;

        public CartHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.product_image_thumbnail);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            removeItem = (TextView) itemView.findViewById(R.id.remove_item);
            productPrice = (TextView)itemView.findViewById(R.id.item_price);
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