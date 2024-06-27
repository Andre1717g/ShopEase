package com.example.shopease.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shopease.LoginActivity;
import com.example.shopease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PerfilFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView tvEmail, tvName, tvCreationDate;
    private ImageView ivProfileImage;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        tvEmail = view.findViewById(R.id.tv_email);
        tvName = view.findViewById(R.id.tv_name);
        tvCreationDate = view.findViewById(R.id.tv_creation_date);
        ivProfileImage = view.findViewById(R.id.iv_profile_image);
        btnLogout = view.findViewById(R.id.btn_logout);

        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();
            String uid = currentUser.getUid();

            if (name != null) {
                tvName.setText(name);
            } else {
                tvName.setText("Nombre de usuario no disponible");
            }

            tvEmail.setText(email);

            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).into(ivProfileImage);
            } else {
                ivProfileImage.setImageResource(R.drawable.perfil);
            }

            // Obtener la fecha de creación del usuario
            currentUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetTokenResult tokenResult = task.getResult();
                                Map<String, Object> claims = tokenResult.getClaims();
                                Object iatObj = claims.get("iat");
                                if (iatObj != null) {
                                    long creationTimestamp = 0;
                                    if (iatObj instanceof Integer) {
                                        creationTimestamp = (Integer) iatObj * 1000L;
                                    } else if (iatObj instanceof Long) {
                                        creationTimestamp = (Long) iatObj * 1000;
                                    }
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    String creationDate = sdf.format(new Date(creationTimestamp));
                                    tvCreationDate.setText(creationDate);
                                }
                            } else {
                                Log.e("PerfilFragment", "Error al obtener el token de usuario", task.getException());
                                Toast.makeText(getContext(), "Error al obtener la fecha de creación", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No se encontraron datos para este usuario", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}
