package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rekap extends AppCompatActivity {

    DatabaseReference db;
    adpRekap adapters;
    ListView listView;
    LottieAnimationView kosong;
    ArrayList<modelRekap> myList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_rekap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Rekap Pengujian");

        db = FirebaseDatabase.getInstance().getReference("rekap");

        kosong = findViewById(R.id.kosong_rekap);
        listView = findViewById(R.id.rvRekap);
        adapters = new adpRekap(Rekap.this);
        myList = new ArrayList<>();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Rekap.this, MainActivity.class);
        intent.putExtra("nik", getIntent().getStringExtra("nik"));
        intent.putExtra("nama", getIntent().getStringExtra("nama"));
        intent.putExtra("c", getIntent().getStringExtra("c"));
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelRekap model = ds.getValue(modelRekap.class);
                    myList.add(model);
                }

                adapters.setList(myList);
                listView.setAdapter(adapters);

                if (myList.size() < 1) {
                    listView.setVisibility(View.GONE);
                    kosong.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    kosong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}