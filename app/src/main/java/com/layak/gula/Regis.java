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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Regis extends AppCompatActivity {

    EditText edID, edNama, edEmail, edPass;
    FirebaseAuth mAuth;
    DatabaseReference db;
    ImageView kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users");

        edID = findViewById(R.id.edID);
        edNama = findViewById(R.id.edNama);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPassword);
        kembali = findViewById(R.id.outRegis);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CircularProgressButton btn = (CircularProgressButton) findViewById(R.id.btn_Regis);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edID.getText().toString().isEmpty()) {
                    edID.requestFocus();
                    edID.setError("Masukkan nik anda");
                } else
                if (edEmail.getText().toString().isEmpty()) {
                    edEmail.requestFocus();
                    edEmail.setError("Masukkan username anda");
                } else
                if (edNama.getText().toString().isEmpty()) {
                    edNama.requestFocus();
                    edNama.setError("Masukkan nama anda");
                } else
                if (edPass.getText().toString().isEmpty()) {
                    edPass.requestFocus();
                    edPass.setError("Masukkan password anda");
                } else {
                    db.orderByChild("username").equalTo(edEmail.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0) {
                                edEmail.requestFocus();
                                edEmail.setError("Username sudah digunakan");
                                return;
                            } else {
                                db.child(edID.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getChildrenCount() > 0) {
                                            edID.requestFocus();
                                            edID.setError("NIK sudah digunakan");
                                            return;
                                        } else {
                                            modelUser model = new modelUser();
                                            model.setNik(edID.getText().toString());
                                            model.setUsername(edEmail.getText().toString());
                                            model.setNama(edNama.getText().toString());
                                            model.setPassword(edPass.getText().toString());
                                            model.setLevel(1);
                                            db.child(edID.getText().toString()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Regis.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Regis.this, Login.class));
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        finish();
    }
}