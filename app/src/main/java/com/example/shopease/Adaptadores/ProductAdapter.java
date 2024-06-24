package com.example.shopease.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.shopease.Models.Product;
import com.example.shopease.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {
    private List<Product> productos;

    public ProductAdapter(List<Product> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Product producto = productos.get(position);
        holder.nombreTextView.setText(producto.getNombre());
        holder.descripcionTextView.setText(producto.getDescripcion());
        holder.precioTextView.setText(String.valueOf(producto.getPrecio()));
        holder.categoriaTextView.setText(producto.getCategoria());
        Glide.with(holder.imgImageView.getContext()).load(producto.getImg()).into(holder.imgImageView);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView, precioTextView, categoriaTextView;
        ImageView imgImageView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.product_name);
            descripcionTextView = itemView.findViewById(R.id.product_description);
            precioTextView = itemView.findViewById(R.id.product_price);
            categoriaTextView = itemView.findViewById(R.id.product_category);
            imgImageView = itemView.findViewById(R.id.product_image);
        }
    }
}
