package com.example.shopease.Adaptadores;

import com.example.shopease.Models.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ProductService {
    @POST("productos")
    Call<Product> saveProducts(@Body Product producto);

    @GET("productos")
    Call<List<Product>> getProductos();
}
