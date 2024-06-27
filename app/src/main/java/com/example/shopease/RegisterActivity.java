package com.example.shopease;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText usuario, correo, contraseña;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ocultar la barra de acción si está presente
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar FirebaseAuth y FirebaseFirestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Esto es para mantener iniciada la sesión
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        // Enlazar las vistas
        usuario = findViewById(R.id.registerUsuario);
        correo = findViewById(R.id.registerCorreo);
        contraseña = findViewById(R.id.registerContraseña);
    }

    public void register(View view) {
        // Obtener y limpiar el texto de los campos
        String userUsuario = usuario.getText().toString().trim();
        String userCorreo = correo.getText().toString().trim();
        String userContraseña = contraseña.getText().toString().trim();

        // Validar los campos
        if (TextUtils.isEmpty(userUsuario)) {
            Toast.makeText(this, "Ingrese un usuario", Toast.LENGTH_SHORT).show();
            return;
        }
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

        // Registrar al usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(userCorreo, userContraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            String userId = auth.getCurrentUser().getUid();

                            // Crear un mapa con la información del usuario
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("userId", userId);
                            userMap.put("username", userUsuario);
                            userMap.put("email", userCorreo);

                            // Guardar la información del usuario en Firestore
                            db.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish(); // Terminar la actividad actual para evitar que el usuario regrese con el botón Atrás
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error al guardar la información del usuario", e);
                                            Toast.makeText(RegisterActivity.this, "Error al registrar el usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(View view) {
        // Ir a la actividad de inicio de sesión
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
