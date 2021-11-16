package com.example.a28popsmall.retrofit;

import com.example.a28popsmall.model.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderEndPoint {

    @POST("posts")
    Call<Order> newOrder(@Body Order order);

}
