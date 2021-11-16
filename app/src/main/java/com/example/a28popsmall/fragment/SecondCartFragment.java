package com.example.a28popsmall.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.a28popsmall.R;
import com.example.a28popsmall.model.GroceryItem;
import com.example.a28popsmall.model.Order;
import com.example.a28popsmall.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondCartFragment extends Fragment {

    public static final String ORDER_KEY = "order";

    private EditText edtTxtAddress, edtTxtZipCode, edtTxtPhoneNumber, edtTxtEmail;
    private Button btnNext, btnBack;
    private TextView txtWarning;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_second, container, false);

        initViews(view);

        //Receive bundle from ThirdCartFragment
        Bundle bundle = getArguments();
        if (null != bundle) {
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (null != jsonOrder) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>() {}.getType();
                Order order = gson.fromJson(jsonOrder, type);
                if (null != order) {
                    edtTxtAddress.setText(order.getAddress());
                    edtTxtPhoneNumber.setText(order.getPhoneNumber());
                    edtTxtZipCode.setText(order.getZipCode());
                    edtTxtEmail.setText(order.getEmail());
                }
            }
        }

        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new FirstCartFragment());
            transaction.commit();
        });

        btnNext.setOnClickListener(v -> {
            if (validateData()) {
                txtWarning.setVisibility(View.GONE);

                ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
                if (null != cartItems) {
                    Order order = new Order();
                    order.setItems(cartItems);
                    order.setAddress(edtTxtAddress.getText().toString());
                    order.setPhoneNumber(edtTxtPhoneNumber.getText().toString());
                    order.setZipCode(edtTxtZipCode.getText().toString());
                    order.setEmail(edtTxtEmail.getText().toString());
                    order.setTotalPrice(calculateTotalPrice(cartItems));

                    Gson gson = new Gson();
                    String jsonOrder = gson.toJson(order);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(ORDER_KEY, jsonOrder);

                    ThirdCartFragment thirdCartFragment = new ThirdCartFragment();
                    thirdCartFragment.setArguments(bundle1);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, thirdCartFragment);
                    transaction.commit();
                }

            }else {
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Please Fill All The Blanks");
            }
        });

        return view;
    }

    private double calculateTotalPrice(ArrayList<GroceryItem> items) {
        double price =0;
        for (GroceryItem item: items) {
            price += item.getPrice();
        }

        price = Math.round(price*100.0)/100.0;

        return price;
    }

    private boolean validateData() {
        if (edtTxtAddress.getText().toString().equals("") || edtTxtPhoneNumber.getText().toString().equals("") ||
                edtTxtZipCode.getText().toString().equals("") || edtTxtEmail.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void initViews(View view) {
        edtTxtAddress = view.findViewById(R.id.edtTxtAddress);
        edtTxtZipCode = view.findViewById(R.id.edtTxtZipCode);
        edtTxtPhoneNumber = view.findViewById(R.id.edtTxtPhoneNumber);
        edtTxtEmail = view.findViewById(R.id.edtTxtEmail);
        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack);
        txtWarning = view.findViewById(R.id.txtWarning);
    }
}
