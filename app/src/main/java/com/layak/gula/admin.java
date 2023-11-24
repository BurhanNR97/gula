package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin extends AppCompatActivity {
    TextView jmlAkun, jmlRekap, jmlSampel, nama;
    CardView rekap, akun, sampel;
    AppCompatButton logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        jmlAkun = findViewById(R.id.jml_akun);
        jmlRekap = findViewById(R.id.jml_rekapp);
        rekap = findViewById(R.id.cvRekapp);
        akun = findViewById(R.id.cvAkun);
        logout = findViewById(R.id.btnLogout);
        sampel = findViewById(R.id.cvSampel);
        jmlSampel = findViewById(R.id.jml_sampel);

        nama = findViewById(R.id.namaAkunn);
        nama.setText(getIntent().getStringExtra("nama"));

        rekap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this, Rekapp.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this, AkunPengguna.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        sampel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this, Sampel.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                intent.putExtra("c", getIntent().getStringExtra("c"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(admin.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Anda ingin logout ?")
                        .setNegativeButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(admin.this, Login.class));
                                overridePendingTransition(R.anim.from_right, R.anim.to_left);
                                finish();
                            }
                        })
                        .setPositiveButton("TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jmlAkun.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.child("rekap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jmlRekap.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.child("sampel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jmlSampel.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}