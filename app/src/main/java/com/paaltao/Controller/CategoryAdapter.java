package com.paaltao.Controller;

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
import com.paaltao.classes.Category;

import java.util.Collections;
import java.util.List;

/**
 * Created by Arindam on 02-Feb-15.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    private LayoutInflater inflater;
    private View view;
    List<Category> data = Collections.emptyList();

    public CategoryAdapter(Context context ,List<Category> data){
        this.data = data;
        this.context= context;
    }
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());

        view = inflater.inflate(R.layout.category_card, parent, false);


        CategoryHolder holder = new CategoryHolder(view);
        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProductDetailsActivity.class);
                context.startActivity(intent);

                }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        Category current = data.get(position);
        holder.categoryName.setText(current.getCategory_name());
        holder.categoryImage.setImageResource(current.getCategory_id());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public CategoryHolder(View itemView) {
            super(itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);

        }
    }
}
