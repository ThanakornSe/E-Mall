package com.example.a28popsmall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a28popsmall.R;
import com.example.a28popsmall.model.GroceryItem;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface TotalPrice{
        //because of total price textView inside a fragment so we need to pass total by call back interface
        void getTotalPrice(double price);
    }

    public interface DeleteItem{
        void onDeleteResult(GroceryItem item);
    }

    private DeleteItem deleteItem;

    private TotalPrice totalPrice;

    private ArrayList<GroceryItem> items = new ArrayList<>();

    private Context context;
    private Fragment fragment;

    public CartAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtName.setText(items.get(position).getName());
        holder.txtPrice.setText(items.get(position).getPrice() + "$");
        holder.txtDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Deleting...")
                    .setMessage("Are you sure you want to delete this item?")
                    .setNegativeButton("No", (dialog, which) -> {})
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            deleteItem = (DeleteItem) fragment;
                            deleteItem.onDeleteResult(items.get(position));
                        }catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    });
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        //use calculate price in here because everytime we change arraylist we need to re calculate price
        calculateTotalPrice();
        notifyDataSetChanged();
    }

    private void calculateTotalPrice() {
        double price =0;
        for (GroceryItem item: items) {
            price += item.getPrice();
        }

        price = Math.round(price*100.0)/100.0;

        try{
            //init call back interface
            totalPrice = (TotalPrice) fragment;
            totalPrice.getTotalPrice(price);
        }catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtPrice, txtDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDelete = itemView.findViewById(R.id.txtDelete);
        }
    }
}
