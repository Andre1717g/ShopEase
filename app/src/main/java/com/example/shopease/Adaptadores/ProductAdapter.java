package com.example.shopease.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> productListFull;
    private CarritoAdapter carritoAdapter;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public ProductAdapter(Context context, List<Product> productList, CarritoAdapter carritoAdapter) {
        this.context = context;
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList); // copia completa de la lista
        this.carritoAdapter = carritoAdapter;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nombre.setText("Nombre: " + product.getNombre());
        holder.descripcion.setText("Descripción: " + product.getDescripcion());
        holder.precio.setText("Precio: $" + String.valueOf(product.getPrecio()));
        holder.categoria.setText("Categoría: " + product.getCategoria());
        Glide.with(holder.itemView.getContext()).load(product.getImg()).into(holder.img);

        holder.addButton.setOnClickListener(v -> {
            addProductToCart(product);
            Toast.makeText(holder.itemView.getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product item : productListFull) {
                    if (item.getNombre().toLowerCase().contains(filterPattern) ||
                            item.getDescripcion().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private void addProductToCart(Product product) {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference carritoRef = databaseReference.child("carritos").child(userId).child("productos");

        carritoRef.child(String.valueOf(product.getId())).setValue(product)
                .addOnSuccessListener(aVoid -> {
                    carritoAdapter.addProductToCart(product); // Asegúrate de que este método esté definido en CarritoAdapter
                    Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al agregar producto", Toast.LENGTH_SHORT).show();
                });
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio, categoria;
        ImageView img;
        Button addButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            descripcion = itemView.findViewById(R.id.descripcion);
            precio = itemView.findViewById(R.id.precio);
            img = itemView.findViewById(R.id.img);
            addButton = itemView.findViewById(R.id.add_to_cart_button);
            categoria = itemView.findViewById(R.id.categoria);
        }
    }
}
