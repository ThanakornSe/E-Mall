package com.example.a28popsmall.adapter;

import static com.example.a28popsmall.activity.GroceryItemActivity.GROCERY_ITEM_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a28popsmall.R;
import com.example.a28popsmall.activity.GroceryItemActivity;
import com.example.a28popsmall.model.GroceryItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    private ArrayList<GroceryItem> items = new ArrayList<>();
    private Context context;


    public GroceryItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GroceryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemAdapter.ViewHolder holder, int position) {
        GroceryItem groceryItem = items.get(position);
        holder.txtName.setText(groceryItem.getName());
        holder.txtPrice.setText(String.valueOf(groceryItem.getPrice()) + "$");
        Glide.with(context)
                .asBitmap()
                .load(groceryItem.getImageUrl())
                .into(holder.image);
        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroceryItemActivity.class);
            intent.putExtra(GROCERY_ITEM_KEY, groceryItem);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtPrice;
        private ImageView image;
        private MaterialCardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            image = itemView.findViewById(R.id.image);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
