package com.example.shopease.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopease.Models.Product;
import com.example.shopease.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageButton productImage;
        public TextView productName;
        public TextView productDescription;
        public TextView productPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        fetchProductsFromFirebase();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentItem = productList.get(position);

        Log.d("ProductAdapter", "Nombre: " + currentItem.getNombre());
        Log.d("ProductAdapter", "Descripción: " + currentItem.getDescripción());
        Log.d("ProductAdapter", "Precio: " + currentItem.getPrecio());


        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.getImg());
        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(holder.productImage)
        ).addOnFailureListener(exception -> {
            // Manejo de errores si no se puede obtener la URL
            holder.productImage.setImageResource(R.drawable.placeholder); // Placeholder image
        });

        holder.productName.setText(currentItem.getNombre());
        holder.productDescription.setText(currentItem.getDescripción());
        holder.productPrice.setText(currentItem.getPrecio());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void fetchProductsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("productos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProductAdapter", "Error al cargar los datos de Firebase", databaseError.toException());
            }
        });
    }
}
