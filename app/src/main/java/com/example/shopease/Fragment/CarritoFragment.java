package com.example.shopease.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.shopease.Adaptadores.CarritoAdapter;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import java.util.List;

public class CarritoFragment extends Fragment {
    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter adapter;
    private List<Product> carritoList;

    public CarritoFragment() {
        this.carritoList = carritoList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        recyclerViewCarrito = view.findViewById(R.id.recyclerViewCarrito);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CarritoAdapter(carritoList);
        recyclerViewCarrito.setAdapter(adapter);

        return view;
    }
}
