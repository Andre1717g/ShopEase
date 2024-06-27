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

        holder.incrementoButton.setOnClickListener(v -> {
            int quantity = product.getCantidad();
            quantity++;
            updateQuantity(holder, product, quantity);
        });

        holder.decrementoButton.setOnClickListener(v -> {
            int quantity = product.getCantidad();
            if (quantity > 1) {
                quantity--;
                updateQuantity(holder, product, quantity);
            }
        });

        holder.eliminarButton.setOnClickListener(v -> {
            eliminarProducto(holder.getAdapterPosition(), product);
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

    private void eliminarProducto(int position, Product product) {
        String userId = auth.getCurrentUser().getUid();
        databaseReference.child("carritos").child(userId).child("productos").child(String.valueOf(product.getId())).removeValue()
                .addOnSuccessListener(aVoid -> {
                    if (position != RecyclerView.NO_POSITION && position < productList.size()) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                        quantityChangeListener.onQuantityChanged();
                    }
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
        ImageButton incrementoButton, decrementoButton, eliminarButton;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.carrito_product_nombre);
            descripcion = itemView.findViewById(R.id.carrito_product_descripcion);
            precio = itemView.findViewById(R.id.carrito_product_precio);
            cantidad = itemView.findViewById(R.id.carrito_cantidad_producto);
            total = itemView.findViewById(R.id.carrito_producto_total_precio);
            img = itemView.findViewById(R.id.carrito_producto_imagen);
            incrementoButton = itemView.findViewById(R.id.incrementarproducto);
            decrementoButton = itemView.findViewById(R.id.restarproducto);
            eliminarButton = itemView.findViewById(R.id.eliminar);
        }
    }
}
