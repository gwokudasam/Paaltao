package com.paaltao.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.activity.ProductDetailsActivity;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Product;
import com.paaltao.logging.L;

import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class TrendingShopAdapter extends RecyclerView.Adapter<TrendingShopAdapter.ProductHolder> {

    private Context context;
    private ProductListActivity activity;
    private LayoutInflater inflater;
    private View view;
    List<Product> data = Collections.emptyList();

    public TrendingShopAdapter(Context context, List<Product> data,ProductListActivity activity){

        inflater = LayoutInflater.from(context);
        this.activity = activity;
        this.data = data;
        this.context= context;
    }
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.trendingshop_row,parent,false);


        ProductHolder holder = new ProductHolder(view);
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context!=null) {
                    context.startActivity(new Intent(context, ProductListActivity.class));
                }
                else{
                    Intent intent = new Intent(activity,ProductListActivity.class);
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
    public void onItemClick(){

    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Product current = data.get(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView productImage;

        public ProductHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }
}
