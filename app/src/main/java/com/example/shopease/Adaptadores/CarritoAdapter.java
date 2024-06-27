package com.example.shopease.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private OnQuantityChangeListener quantityChangeListener;

    public CarritoAdapter(List<Product> productList, OnQuantityChangeListener quantityChangeListener) {
        this.productList = productList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nombre.setText(product.getNombre());
        holder.descripcion.setText(product.getDescripcion());
        holder.precio.setText("Precio: $" + product.getPrecio());
        Glide.with(holder.itemView.getContext()).load(product.getImg()).into(holder.img);

        holder.cantidad.setText("Cantidad: " + product.getCantidad());
        holder.total.setText("Total: $" + (product.getPrecio() * product.getCantidad()));

        holder.increaseButton.setOnClickListener(v -> {
            int quantity = product.getCantidad();
            quantity++;
            updateQuantity(holder, product, quantity);
        });

        holder.decreaseButton.setOnClickListener(v -> {
            int quantity = product.getCantidad();
            if (quantity > 1) {
                quantity--;
                updateQuantity(holder, product, quantity);
            }
        });
    }

    private void updateQuantity(CarritoViewHolder holder, Product product, int quantity) {
        String userId = auth.getCurrentUser().getUid();
        product.setCantidad(quantity);
        databaseReference.child("carritos").child(userId).child("productos").child(String.valueOf(product.getId())).setValue(product)
                .addOnSuccessListener(aVoid -> {
                    holder.cantidad.setText("Cantidad: " + quantity);
                    holder.total.setText("Total: $" + (product.getPrecio() * quantity));
                    quantityChangeListener.onQuantityChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void addProductToCart(Product product) {
        if (!productList.contains(product)) {
            productList.add(product);
            notifyDataSetChanged();
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio, cantidad, total;
        ImageView img;
        ImageButton increaseButton, decreaseButton;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.carrito_product_name);
            descripcion = itemView.findViewById(R.id.carrito_product_description);
            precio = itemView.findViewById(R.id.carrito_product_price);
            cantidad = itemView.findViewById(R.id.carrito_product_quantity);
            total = itemView.findViewById(R.id.carrito_product_total_price);
            img = itemView.findViewById(R.id.carrito_product_image);
            increaseButton = itemView.findViewById(R.id.button_increase_quantity);
            decreaseButton = itemView.findViewById(R.id.button_decrease_quantity);
        }
    }
}
