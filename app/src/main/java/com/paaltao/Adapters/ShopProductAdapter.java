package com.paaltao.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.classes.Product;

import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class ShopProductAdapter  extends RecyclerView.Adapter<ShopProductAdapter.ShopProductHolder> {
private Context context;
private LayoutInflater inflater;
private View view;
        List<Product> data = Collections.emptyList();

public ShopProductAdapter(Context context ,List<Product> data){
        this.data = data;
        this.context= context;
        }
@Override
public ShopProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.shop_product, parent, false);


        ShopProductHolder holder = new ShopProductHolder(view);

        return holder;
        }



    @Override
public void onBindViewHolder(ShopProductHolder holder, int position) {

        Product current = data.get(position);
        holder.shopProductName.setText(current.getProduct_name());


        }



    @Override
public int getItemCount() {
        return data.size();
        }

class ShopProductHolder extends RecyclerView.ViewHolder {
    TextView shopProductName;
    ImageView shopProductImage;

    public ShopProductHolder(View itemView) {
        super(itemView);

        shopProductImage = (ImageView) itemView.findViewById(R.id.shop_product_image);
        shopProductName = (TextView) itemView.findViewById(R.id.shop_product_name);

    }
}
}