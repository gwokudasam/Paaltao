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
import com.paaltao.classes.Product;

import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ProductHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Product> data = Collections.emptyList();

    public FeaturedProductAdapter(Context context, List<Product> data){

        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context= context;
    }
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.featured_product_row,parent,false);


        ProductHolder holder = new ProductHolder(view);
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductDetailsActivity.class));
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Product current = data.get(position);
        holder.productName.setText(current.getProduct_name());
        holder.productImage.setImageResource(current.getProduct_id());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage;

        public ProductHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
        }
    }
}
