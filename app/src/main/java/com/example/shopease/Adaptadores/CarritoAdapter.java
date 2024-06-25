package com.example.shopease.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.Models.Product;
import com.example.shopease.R;

import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private List<Product> carritoList;

    public CarritoAdapter(List<Product> carritoList) {
        if (carritoList == null) {
            this.carritoList = new ArrayList<>(); // Inicializa la lista si es nula
        } else {
            this.carritoList = carritoList;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = carritoList.get(position);
        holder.nombre.setText(product.getNombre());
        holder.precio.setText(String.valueOf(product.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView precio;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.carrito_product_name);
            precio = itemView.findViewById(R.id.carrito_product_price);
        }
    }
}
