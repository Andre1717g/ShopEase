package com.example.shopease;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.shopease.Fragment.HomeFragment;
import com.example.shopease.Fragment.CarritoFragment;
import com.example.shopease.Fragment.CategoriaFragment;
import com.example.shopease.Fragment.PerfilFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentContainerView fragmentContainerView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Verificar si el usuario ha iniciado sesión
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return; // Evitar continuar con la inicialización si el usuario no ha iniciado sesión
        }

        bottomNavigationView = findViewById(R.id.BNavigation);
        fragmentContainerView = findViewById(R.id.fragment_container_view);

        // Cargar el fragmento predeterminado (HomeFragment) si savedInstanceState es nulo
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Inicio":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new HomeFragment()).commit();
                    break;
                case "Carrito":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new CarritoFragment()).commit();
                    break;
                case "Categoría":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new CategoriaFragment()).commit();
                    break;
                case "Perfil":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new PerfilFragment()).commit();
                    break;
            }
            return true;
        });
    }
}
