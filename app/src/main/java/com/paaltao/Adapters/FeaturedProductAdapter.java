package com.paaltao.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.paaltao.R;
import com.paaltao.activity.ProductDetailsActivity;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.logging.L;
import com.paaltao.network.VolleySingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ProductHolder> {

    private Context context;
    private ProductListActivity activity;
    private LayoutInflater inflater;
    private View view;
    private VolleySingleton singleton;
    private ImageLoader imageLoader;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    public FeaturedProductAdapter(Context context,ProductListActivity activity){

        inflater = LayoutInflater.from(context);
        this.activity = activity;
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
        this.context= context;

    }
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.featured_product_row, parent, false);


        ProductHolder holder = new ProductHolder(view);
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context!=null) {
                    context.startActivity(new Intent(context, ProductDetailsActivity.class));
                }
                else{
                    Intent intent = new Intent(activity,ProductDetailsActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
        initialize();
        onItemClick();
        return holder;
    }

    public void initialize(){

    }
    public void setProductArrayList(ArrayList<Product> productArrayList){
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, productArrayList.size());
    }
    public void onItemClick(){

    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        Product current = productArrayList.get(position);
        holder.productName.setText(current.getProduct_name());
        holder.productPrice.setText(current.getPrice());

        String imageURL = current.getImageURL();

        if(imageURL != null){
            imageLoader.get(imageURL, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    holder.productImage.setImageBitmap(imageContainer.getBitmap());

                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice;
        ImageView productImage;

        public ProductHolder(View itemView) {
            super(itemView);

            productPrice = (TextView)itemView.findViewById(R.id.product_price);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
        }
    }
}
