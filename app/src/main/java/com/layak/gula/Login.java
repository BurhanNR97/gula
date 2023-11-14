package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    ImageView regis;
    EditText username, password;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        db = FirebaseDatabase.getInstance().getReference("users");

        regis = findViewById(R.id.keRegis);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Regis.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                finish();
            }
        });

        CircularProgressButton btn = (CircularProgressButton) findViewById(R.id.btn_Login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()) {
                    username.requestFocus();
                    username.setError("Masukkan username");
                } else
                if (password.getText().toString().isEmpty()) {
                    password.requestFocus();
                    password.setError("Masukkan password");
                } else {
                    db.orderByChild("username").equalTo(username.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() <= 0) {
                                Toast.makeText(Login.this, "Username/password salah", Toast.LENGTH_SHORT).show();
                            } else {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    modelUser model = ds.getValue(modelUser.class);
                                    if (model.getPassword().equals(password.getText().toString())) {
                                        if (model.getLevel() == 1) {
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("nik", model.getNik());
                                            intent.putExtra("nama", model.getNama());
                                            overridePendingTransition(R.anim.from_left, R.anim.to_right);
                                            startActivity(intent);
                                            finish();
                                        } else
                                        if (model.getLevel() == 0) {
                                            Intent intent = new Intent(Login.this, admin.class);
                                            intent.putExtra("nik", model.getNik());
                                            intent.putExtra("nama", model.getNama());
                                            overridePendingTransition(R.anim.from_left, R.anim.to_right);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}