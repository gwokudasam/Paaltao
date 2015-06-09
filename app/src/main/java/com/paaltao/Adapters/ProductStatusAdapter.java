package com.paaltao.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paaltao.R;
import com.paaltao.activity.ProductStatus;
import com.paaltao.classes.Product;

import java.util.ArrayList;

/**
 * Created by arindam.paaltao on 09-Jun-15.
 */
public class ProductStatusAdapter extends RecyclerView.Adapter<ProductStatusAdapter.ProductHolder> {

    private ClickListener clickListener;
    private Context context;
    private ProductStatus activity;
    private View view;
    LayoutInflater inflater;
    private ArrayList<Product> productStatusArrayList = new ArrayList<>();


    public ProductStatusAdapter(Context context,ProductStatus activity){
        this.activity = activity;
        this.context= context;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.product_status_row, parent, false);


        ProductHolder holder = new ProductHolder(view);

        return holder;
    }

    public void setProductStatusArrayList(ArrayList<Product> productStatusArrayList){
        this.productStatusArrayList = productStatusArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, productStatusArrayList.size());
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        Product current = productStatusArrayList.get(position);
        holder.productName.setText(current.getProduct_name());
        holder.productPrice.setText(current.getPrice());
        holder.status.setText(current.getStatus());
        holder.uploadedDate.setText(current.getUploadedDate());


    }

    @Override
    public int getItemCount() {
        return productStatusArrayList.size();
    }


    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView productName,productPrice,uploadedDate,status;

        public ProductHolder(View itemView) {
            super(itemView);

            productName = (TextView)itemView.findViewById(R.id.product_name);
            productPrice = (TextView)itemView.findViewById(R.id.product_price);
            uploadedDate = (TextView)itemView.findViewById(R.id.uploaded_date);
            status = (TextView)itemView.findViewById(R.id.status);
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
