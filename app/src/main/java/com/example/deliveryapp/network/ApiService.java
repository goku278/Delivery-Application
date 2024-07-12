package com.example.deliveryapp.network;

import com.example.deliveryapp.model.ResponseList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *  An interface which has the api call GET
 */

public interface ApiService {
    @GET("orderlist.php")
    Call<ResponseList> getOrderList();
}
