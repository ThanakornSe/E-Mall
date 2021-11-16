package com.example.a28popsmall.activity;

import static com.example.a28popsmall.dialog.AllCategoriesDialog.ALL_CATEGORIES;
import static com.example.a28popsmall.dialog.AllCategoriesDialog.CALLING_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.a28popsmall.R;
import com.example.a28popsmall.adapter.GroceryItemAdapter;
import com.example.a28popsmall.databinding.ActivitySearchBinding;
import com.example.a28popsmall.dialog.AllCategoriesDialog;
import com.example.a28popsmall.model.GroceryItem;
import com.example.a28popsmall.util.Utils;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AllCategoriesDialog.GetCategory {
    private ActivitySearchBinding binding;
    private GroceryItemAdapter adapter;

    @Override
    public void onGetCategoryResult(String category) {
        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getApplication(),category);
        if (items != null) {
            adapter.setItems(items);
            increaseUserPoint(items);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);

        initBottomNavView();
        setSupportActionBar(binding.toolbar);

        adapter = new GroceryItemAdapter(this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerView.setAdapter(adapter);

        //get data from allCategoriesDialog when it call from MainActivity
        Intent intent = getIntent();
        if (null != intent) {
            String category = intent.getStringExtra("category");
            if (null != category) {
                ArrayList<GroceryItem> items = Utils.getItemsByCategory(this, category);
                if (null != items) {
                    adapter.setItems(items);
                    increaseUserPoint(items);
                }
            }
        }

        binding.btnSearch.setOnClickListener(v -> initSearch());

        binding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { initSearch(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        ArrayList<String> categories = Utils.getCategories(this);
        if (categories != null) {
            if (categories.size() > 0) {
                if (categories.size() == 1) {
                    showCategories(categories,1);
                } else if (categories.size() == 2) {
                    showCategories(categories,2);
                }else {
                    showCategories(categories,3);
                }
            }
        }

        binding.txtAllCategories.setOnClickListener(v -> {
            AllCategoriesDialog dialog = new AllCategoriesDialog();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ALL_CATEGORIES, Utils.getCategories(SearchActivity.this));
            bundle.putString(CALLING_ACTIVITY, "search_activity");
            dialog.setArguments(bundle);
            dialog.show(SearchActivity.this.getSupportFragmentManager(), "all categories dialog");
        });
    }

    private void increaseUserPoint(ArrayList<GroceryItem> items) {
        for (GroceryItem i: items) {
            Utils.changeUserPoint(this, i, 1);
        }
    }

    private void showCategories(ArrayList<String> categories, int i) {
        switch (i){
            case 1:
                binding.txtFirstCat.setVisibility(View.VISIBLE);
                binding.txtFirstCat.setText(categories.get(0));
                binding.txtSecondCat.setVisibility(View.GONE);
                binding.txtThirdCat.setVisibility(View.GONE);
                binding.txtFirstCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                break;
            case 2:
                binding.txtFirstCat.setVisibility(View.VISIBLE);
                binding.txtFirstCat.setText(categories.get(0));
                binding.txtSecondCat.setVisibility(View.VISIBLE);
                binding.txtSecondCat.setText(categories.get(1));
                binding.txtThirdCat.setVisibility(View.GONE);
                binding.txtFirstCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                binding.txtSecondCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                break;
            case 3:
                binding.txtFirstCat.setVisibility(View.VISIBLE);
                binding.txtFirstCat.setText(categories.get(0));
                binding.txtSecondCat.setVisibility(View.VISIBLE);
                binding.txtSecondCat.setText(categories.get(1));
                binding.txtThirdCat.setVisibility(View.VISIBLE);
                binding.txtThirdCat.setText(categories.get(2));
                binding.txtFirstCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                binding.txtSecondCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                binding.txtThirdCat.setOnClickListener(v -> {
                    ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(2));
                    if (null != items) {
                        adapter.setItems(items);
                        increaseUserPoint(items);
                    }
                });
                break;
            default:
                binding.txtFirstCat.setVisibility(View.GONE);
                binding.txtSecondCat.setVisibility(View.GONE);
                binding.txtThirdCat.setVisibility(View.GONE);
                break;
        }
    }

    private void initSearch() {
        if(!TextUtils.isEmpty(binding.searchBox.getText())){
            String name = binding.searchBox.getText().toString();
            ArrayList<GroceryItem> items = Utils.searchForItems(this,name);
            if (items != null) {
                adapter.setItems(items);
                increaseUserPoint(items);
            }
        }
    }

    private void initBottomNavView() {
        binding.bottomNavView.setSelectedItemId(R.id.search);
        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.search:
                        break;
                    case R.id.cart:
                        Intent cartIntent = new Intent(SearchActivity.this, CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}