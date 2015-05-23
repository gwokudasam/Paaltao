package com.paaltao.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.paaltao.R;
import com.paaltao.activity.ShopActivity;
import com.paaltao.classes.Product;
import com.paaltao.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by Arindam on 30-Jan-15.
 */
public class TrendingShopAdapter extends RecyclerView.Adapter<TrendingShopAdapter.ProductHolder> {

    private Context context;
    private ShopActivity activity;
    private LayoutInflater inflater;
    private View view;
    private ClickListener clickListener;
    private VolleySingleton singleton;
    private ImageLoader imageLoader;
    private ArrayList<Product> shopArrayList = new ArrayList<>();

    public TrendingShopAdapter(Context context,ShopActivity activity){
        this.activity = activity;
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
        this.context= context;
    }
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.trendingshop_row, parent, false);


        ProductHolder holder = new ProductHolder(view);

        return holder;
    }

    public void setShopArrayList(ArrayList<Product> shopArrayList){
        this.shopArrayList = shopArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, shopArrayList.size());
    }
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        Product current = shopArrayList.get(position);
        holder.shopName.setText(current.getShop_name());

        String shop_image_url = current.getShop_image_url();

        if(shop_image_url != null){
            imageLoader.get(shop_image_url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    holder.shopImage.setImageBitmap(imageContainer.getBitmap());

                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView shopImage;
        TextView shopName;

        public ProductHolder(View itemView) {
            super(itemView);

            shopImage = (ImageView) itemView.findViewById(R.id.shop_image);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v, getLayoutPosition());
            }
        }
    }
    public interface ClickListener{
        void itemClicked(View view, int position);

    }
}
