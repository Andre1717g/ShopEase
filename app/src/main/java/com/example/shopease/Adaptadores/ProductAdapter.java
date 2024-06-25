package com.example.shopease.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import com.squareup.picasso.Picasso;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> productListFull; // Lista completa para el filtrado
    private List<Product> carritoList; // Lista de productos del carrito

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

    public ProductAdapter(List<Product> productList, List<Product> carritoList) {
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList);
        this.carritoList = carritoList;
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

        holder.productName.setText(currentItem.getNombre());
        holder.productDescription.setText(currentItem.getDescripcion());
        holder.productPrice.setText("$" + currentItem.getPrecio());

        Picasso.get().load(currentItem.getImg()).into(holder.productImage);

        holder.itemView.findViewById(R.id.btn_carrito).setOnClickListener(v -> {
            carritoList.add(currentItem);
            Toast.makeText(holder.itemView.getContext(), "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
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
