package com.example.a28popsmall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.a28popsmall.R;
import com.example.a28popsmall.TrackUserTime;
import com.example.a28popsmall.adapter.ReviewsAdapter;
import com.example.a28popsmall.databinding.ActivityGroceryItemBinding;
import com.example.a28popsmall.dialog.AddReviewDialog;
import com.example.a28popsmall.model.GroceryItem;
import com.example.a28popsmall.model.Review;
import com.example.a28popsmall.util.Utils;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {

    public static final String GROCERY_ITEM_KEY = "incoming_item";
    private ActivityGroceryItemBinding binding;
    private GroceryItem incomingItem;
    private ReviewsAdapter adapter;

    private boolean isBound;
    private TrackUserTime mService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder binder = (TrackUserTime.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_item);

        setSupportActionBar(binding.toolbar);

        adapter = new ReviewsAdapter();

        Intent intent = getIntent();
        if (intent != null) {
            incomingItem = intent.getParcelableExtra(GROCERY_ITEM_KEY);
            if (incomingItem != null) {
                //add userpoint 1 whenever user watch the item
                Utils.changeUserPoint(this,incomingItem,1);
                binding.txtName.setText(incomingItem.getName());
                binding.txtDescription.setText(incomingItem.getDescription());
                binding.txtPrice.setText(String.valueOf(incomingItem.getPrice()));
                Glide.with(this)
                        .asBitmap()
                        .load(incomingItem.getImageUrl())
                        .into(binding.itemImage);
                //because parcelable in model class not write an arraylist<Review> so we have to get it by ourself from utils
                ArrayList<Review> reviews = Utils.getReviewById(this,incomingItem.getId());
                binding.reviewsRecView.setAdapter(adapter);
                binding.reviewsRecView.setLayoutManager(new LinearLayoutManager(this));

                if (null != reviews) {
                    if (reviews.size() > 0) {
                        adapter.setReviews(reviews);
                    }
                }
                binding.btnAddToCart.setOnClickListener(v -> {
                    Utils.addItemToCart(this,incomingItem);
                    Intent cartIntent = new Intent(GroceryItemActivity.this, CartActivity.class);
                    cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cartIntent);

                });

                binding.txtAddReview.setOnClickListener(v -> {
                    AddReviewDialog dialog = new AddReviewDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(GROCERY_ITEM_KEY,incomingItem);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(),"add review");
                });

                handleRating();
            }
        }
    }

    private void handleRating() {
        switch (incomingItem.getRate()) {
            case 0:
                binding.firstEmptyStar.setVisibility(View.VISIBLE);
                binding.firstFilledStar.setVisibility(View.GONE);
                binding.secondEmptyStar.setVisibility(View.VISIBLE);
                binding.secondFilledStar.setVisibility(View.GONE);
                binding.thirdEmptyStar.setVisibility(View.VISIBLE);
                binding.thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                binding.firstEmptyStar.setVisibility(View.GONE);
                binding.firstFilledStar.setVisibility(View.VISIBLE);
                binding.secondEmptyStar.setVisibility(View.VISIBLE);
                binding.secondFilledStar.setVisibility(View.GONE);
                binding.thirdEmptyStar.setVisibility(View.VISIBLE);
                binding.thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                binding.firstEmptyStar.setVisibility(View.GONE);
                binding.firstFilledStar.setVisibility(View.VISIBLE);
                binding.secondEmptyStar.setVisibility(View.GONE);
                binding.secondFilledStar.setVisibility(View.VISIBLE);
                binding.thirdEmptyStar.setVisibility(View.VISIBLE);
                binding.thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                binding.firstEmptyStar.setVisibility(View.GONE);
                binding.firstFilledStar.setVisibility(View.VISIBLE);
                binding.secondEmptyStar.setVisibility(View.GONE);
                binding.secondFilledStar.setVisibility(View.VISIBLE);
                binding.thirdEmptyStar.setVisibility(View.GONE);
                binding.thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        binding.firstStarRelLayout.setOnClickListener(v -> {
            if (incomingItem.getRate() != 1) {
                Utils.changeRate(this, incomingItem.getId(), 1);

                Utils.changeUserPoint(this, incomingItem, (1-incomingItem.getRate())*2);

                //set rate because of our click
                incomingItem.setRate(1);
                handleRating();
            }
        });
        binding.secondStarRelLayout.setOnClickListener(v -> {
            if (incomingItem.getRate() != 2) {
                Utils.changeRate(this, incomingItem.getId(), 2);
                Utils.changeUserPoint(this, incomingItem, (2-incomingItem.getRate())*2);
                incomingItem.setRate(2);
                handleRating();
            }
        });
        binding.thirdStarRelLayout.setOnClickListener(v -> {
            if (incomingItem.getRate() != 3) {
                Utils.changeRate(this, incomingItem.getId(), 3);
                Utils.changeUserPoint(this, incomingItem, (3-incomingItem.getRate())*2);
                incomingItem.setRate(3);
                handleRating();
            }
        });
    }

    @Override
    public void onAddReviewResult(Review review) {
        Utils.addReview(this, review);
        Utils.changeUserPoint(this, incomingItem, 3);
        ArrayList<Review> reviews = Utils.getReviewById(this, review.getGroceryItemId());
        if (null != reviews) {
            adapter.setReviews(reviews);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,TrackUserTime.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
        }
    }
}