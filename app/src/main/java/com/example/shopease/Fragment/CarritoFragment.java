package com.example.shopease.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.Adaptadores.CarritoAdapter;
import com.example.shopease.Models.Product;
import com.example.shopease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CarritoFragment extends Fragment {
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private TextView totalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        totalTextView = view.findViewById(R.id.header_text);
        recyclerView = view.findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productList = new ArrayList<>();
        adapter = new CarritoAdapter(productList, this::updateTotal);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cargarProductosCarrito();

        return view;
    }

    private void cargarProductosCarrito() {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference carritoRef = databaseReference.child("carritos").child(userId).child("productos");

        carritoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }
                adapter.notifyDataSetChanged();
                updateTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CarritoFragment", "Error al obtener carrito", databaseError.toException());
                Toast.makeText(getContext(), "Error al obtener carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotal() {
        double total = 0.0;
        for (Product product : productList) {
            total += product.getPrecio() * product.getCantidad();
        }
        totalTextView.setText("Total a pagar: $" + String.format("%.2f", total));
    }
}
