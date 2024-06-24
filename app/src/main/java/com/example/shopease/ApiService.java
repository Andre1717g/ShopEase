package com.example.shopease;

import com.example.shopease.Models.Product;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ApiService {
    @GET("api/productos")
    Call<List<Product>> getProductos();
}
