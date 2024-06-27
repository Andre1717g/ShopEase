package com.example.shopease.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.Adaptadores.ApiClient;
import com.example.shopease.Adaptadores.CarritoAdapter;
import com.example.shopease.Adaptadores.ProductAdapter;
import com.example.shopease.Adaptadores.ProductService;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.widget.SearchView;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private CarritoAdapter carritoAdapter;
    private List<Product> productList;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carritoAdapter = new CarritoAdapter(new ArrayList<>(), this::updateTotal); // Inicializa el CarritoAdapter

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        ProductService apiService = ApiClient.getClient().create(ProductService.class);
        Call<List<Product>> call = apiService.getProductos();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    adapter = new ProductAdapter(getContext(), productList, carritoAdapter);
                    recyclerView.setAdapter(adapter);
                    setupSearchView();
                } else {
                    Log.e("HomeFragment", "Error: " + response.code());
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Fallo: " + t.getMessage());
                showToast("Conexión fallida: " + t.getMessage());
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private void showToast(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotal() {

    }
}
