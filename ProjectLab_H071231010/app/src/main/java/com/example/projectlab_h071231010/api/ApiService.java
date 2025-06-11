package com.example.projectlab_h071231010.api;

import com.example.projectlab_h071231010.model.PizzaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY  = "1e99466161msh8c0b8eacc4ad5f5p188a78jsnb0c80df94fd0";
    String RAPID_API_HOST = "pizzaallapala.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("productos")
    Call<PizzaResponse> getPizza();
}
