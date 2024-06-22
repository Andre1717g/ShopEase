package com.example.shopease.Models;

public class Product {
    private String nombre;
    private String descripción;
    private String img;
    private String precio;

    public Product() {
        // Constructor vacío necesario para Firebase
    }

    public Product(String nombre, String descripción, String img, String precio) {
        this.nombre = nombre;
        this.descripción = descripción;
        this.img = img;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
