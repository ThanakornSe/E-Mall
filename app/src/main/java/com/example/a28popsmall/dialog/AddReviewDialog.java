package com.example.a28popsmall.dialog;

import static android.view.View.*;
import static android.view.View.GONE;
import static com.example.a28popsmall.activity.GroceryItemActivity.GROCERY_ITEM_KEY;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.a28popsmall.R;
import com.example.a28popsmall.activity.GroceryItemActivity;
import com.example.a28popsmall.model.GroceryItem;
import com.example.a28popsmall.model.Review;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReviewDialog extends DialogFragment {

    public interface AddReview {
        void onAddReviewResult(Review review);
    }

    private AddReview addReview;
    private TextView txtItemName, txtWarning;
    private EditText edtTxtUserName;
    private EditText edtTxtReview;
    private Button btnAddReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_review, null);
        initViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final GroceryItem item = bundle.getParcelable(GROCERY_ITEM_KEY);
            if (item != null) {
                txtItemName.setText(item.getName());
                btnAddReview.setOnClickListener(v -> {
                    String userName = edtTxtUserName.getText().toString();
                    String review = edtTxtReview.getText().toString();
                    String date = getCurrentDate();

                    if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(review)){
                        txtWarning.setText("Fill all the blanks");
                        txtWarning.setVisibility(txtWarning.getVisibility()==GONE?VISIBLE:GONE);
                    }else{
                        try {
                            addReview = (AddReview) getActivity();
                            addReview.onAddReviewResult(new Review(item.getId(),userName,review,date));
                            dismiss();
                        }catch (ClassCastException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        return builder.create();
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY");
        return sdf.format(calendar.getTime());
    }

    private void initViews(View view) {
        txtItemName = view.findViewById(R.id.txtItemName);
        txtWarning = view.findViewById(R.id.txtWarning);
        edtTxtUserName = view.findViewById(R.id.edtTxtUserName);
        edtTxtReview = view.findViewById(R.id.edtTxtReview);
        btnAddReview = view.findViewById(R.id.btnAddReview);
    }

}
