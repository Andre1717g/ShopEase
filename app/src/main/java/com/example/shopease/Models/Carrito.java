package com.example.shopease.Models;

import java.util.List;

public class Carrito {
    private String userId;
    private List<Product> productos;

    // Constructor vacío necesario para Firestore
    public Carrito() {
    }

    public Carrito(String userId, List<Product> productos) {
        this.userId = userId;
        this.productos = productos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProductos() {
        return productos;
    }

    public void setProductos(List<Product> productos) {
        this.productos = productos;
    }
}
