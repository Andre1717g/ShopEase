package com.example.shopease.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements android.widget.Filterable {
    private List<Product> productList;
    private List<Product> productListFull; // Para guardar la lista completa de productos

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageButton productImage;
        public TextView productName;
        public TextView productDescription;
        public TextView productPrice;
        public Button addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.btn_carrito);
        }
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList); // Inicializamos la lista completa
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
        Log.d("ProductAdapter", "Descripcion: " + currentItem.getDescripcion());
        Log.d("ProductAdapter", "Precio: " + currentItem.getPrecio());

        Picasso.get().load(currentItem.getImg()).into(holder.productImage);

        holder.productName.setText(currentItem.getNombre());
        holder.productDescription.setText(currentItem.getDescripcion());
        holder.productPrice.setText("$" + currentItem.getPrecio());

        holder.addToCartButton.setOnClickListener(v -> {
            // Manejo del evento de agregar al carrito
            // Implementa la lógica para agregar al carrito aquí

        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProductList(List<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        productListFull = new ArrayList<>(newProductList); // Actualizamos la lista completa cuando cargamos los datos
        notifyDataSetChanged();
    }

    @Override
    public android.widget.Filter getFilter() {
        return productFilter;
    }

    private android.widget.Filter productFilter = new android.widget.Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product item : productListFull) {
                    if (item.getNombre().toLowerCase().contains(filterPattern)) {
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
}
