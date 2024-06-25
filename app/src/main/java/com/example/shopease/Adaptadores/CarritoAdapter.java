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
import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {
    private List<Product> carritoList;

    public CarritoAdapter(List<Product> carritoList) {
        this.carritoList = carritoList;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Product product = carritoList.get(position);
        holder.nombre.setText(product.getNombre());
        holder.descripcion.setText(product.getDescripcion());
        holder.precio.setText(String.valueOf(product.getPrecio()));
        Glide.with(holder.itemView.getContext()).load(product.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }

    public void addProduct(Product product) {
        carritoList.add(product);
        notifyDataSetChanged();
    }

    public void addProductToCart(Product product) {
        carritoList.add(product);
        notifyDataSetChanged();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio;
        ImageView img;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            descripcion = itemView.findViewById(R.id.descripcion);
            precio = itemView.findViewById(R.id.precio);
            img = itemView.findViewById(R.id.img);
        }
    }
}
