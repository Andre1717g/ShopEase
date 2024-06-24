package com.example.shopease.ApiRest;// ApiClient.java
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import java.util.List;
import retrofit2.Call;
import com.example.shopease.Models.Product;


public class ApiClient {
    private static final String BASE_URL = "http://127.0.0.1:8000/api/productos/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


