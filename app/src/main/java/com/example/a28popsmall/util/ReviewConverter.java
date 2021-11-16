package com.example.a28popsmall.util;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.a28popsmall.model.Review;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewConverter {
    @TypeConverter
    public String reviewsToJson(ArrayList<Review> reviews){
        Gson gson = new Gson();
        return gson.toJson(reviews);
    }

    @TypeConverter
    public ArrayList<Review> jsonToReviews(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Review>>() {}.getType();
        return gson.fromJson(json,type);
    }
}
