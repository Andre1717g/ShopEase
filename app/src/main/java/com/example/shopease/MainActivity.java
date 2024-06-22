package com.example.shopease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.shopease.Fragment.HomeFragment;
import com.example.shopease.Fragment.CarritoFragment;
import com.example.shopease.Fragment.CategoriaFragment;
import com.example.shopease.Fragment.PerfilFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.BNavigation);
        fragmentContainerView = findViewById(R.id.fragment_container_view);

        // Load the default fragment (HomeFragment) if the savedInstanceState is null
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
