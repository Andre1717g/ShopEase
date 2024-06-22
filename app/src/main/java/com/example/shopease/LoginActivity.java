package com.example.shopease;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText correo, contraseña;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ocultar la barra de acción si está presente
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.loginCorreo);
        contraseña = findViewById(R.id.loginContraseña);
    }

    public void login(View view) {
        String userCorreo = correo.getText().toString().trim();
        String userContraseña = contraseña.getText().toString().trim();

        if (TextUtils.isEmpty(userCorreo)) {
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userContraseña)) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userContraseña.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Intentar iniciar sesión con Firebase Authentication
        auth.signInWithEmailAndPassword(userCorreo, userContraseña)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Terminar la actividad actual para evitar que el usuario regrese con el botón Atrás
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
