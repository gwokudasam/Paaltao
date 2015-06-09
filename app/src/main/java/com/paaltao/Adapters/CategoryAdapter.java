package com.paaltao.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.paaltao.R;
import com.paaltao.activity.CategoryActivity;
import com.paaltao.activity.ProductDetailsActivity;
import com.paaltao.activity.ProductListActivity;
import com.paaltao.classes.Category;
import com.paaltao.classes.Product;
import com.paaltao.network.VolleySingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 02-Feb-15.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    CategoryActivity activity;
    private LayoutInflater inflater;
    private View view;
    private VolleySingleton singleton;
    private ImageLoader imageLoader;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ClickListener clickListener;

    public CategoryAdapter(CategoryActivity activity, Context context){
        this.context = context;
        this.activity = activity;
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.category_card, parent, false);

        CategoryHolder holder = new CategoryHolder(view);

        return holder;
    }
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList){
        this.categoryArrayList = categoryArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, categoryArrayList.size());
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, int position) {

        Category current = categoryArrayList.get(position);
        holder.categoryName.setText(current.getCategory_name());

        final String imageURL = current.getImageURL();

        if(imageURL != null){
            imageLoader.get(imageURL, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    holder.categoryImage.setImageBitmap(imageContainer.getBitmap());
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
        return categoryArrayList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName;
        ImageView categoryImage;

        public CategoryHolder(View itemView) {
            super(itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
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
