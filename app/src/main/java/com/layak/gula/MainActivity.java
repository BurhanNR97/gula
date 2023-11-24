package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    TextView nama, jmlSampel, jmlRekap;
    CardView uji, sampel, rekap;
    AppCompatButton keluar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.biru));
        }

        nama = findViewById(R.id.namaAkun);
        nama.setText(getIntent().getStringExtra("nama"));
        jmlSampel = findViewById(R.id.jml_sampel);
        jmlRekap = findViewById(R.id.jml_rekap);

        uji = findViewById(R.id.cvUji);
        uji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Uji.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                intent.putExtra("c", getIntent().getStringExtra("c"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        sampel = findViewById(R.id.cvSampel);
        sampel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Sampel.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                intent.putExtra("c", getIntent().getStringExtra("c"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        rekap = findViewById(R.id.cvRekap);
        rekap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Rekap.class);
                intent.putExtra("nik", getIntent().getStringExtra("nik"));
                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                intent.putExtra("c", getIntent().getStringExtra("c"));
                overridePendingTransition(R.anim.from_left, R.anim.to_right);
                startActivity(intent);
                finish();
            }
        });

        keluar = findViewById(R.id.btnLogout);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Anda ingin logout ?")
                        .setNegativeButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, Login.class));
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
        db.child("sampel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jmlSampel.setText("" + snapshot.getChildrenCount());
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
    }
}