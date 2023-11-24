package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AkunPengguna extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DatabaseReference db;
    FloatingActionButton fab;
    adpUser adapters;
    ListView listView;
    LottieAnimationView kosong;
    ArrayList<modelUser> mylist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_pengguna);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_akun);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Akun Pengguna");

        db = FirebaseDatabase.getInstance().getReference("users");
        fab = findViewById(R.id.fabAkun);
        adapters = new adpUser(AkunPengguna.this);
        listView = findViewById(R.id.rvAkun);
        kosong = findViewById(R.id.kosong_akun);
        mylist = new ArrayList<>();

        listView.setOnItemClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AkunPengguna.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(AkunPengguna.this).inflate(R.layout.dialog_akun, (LinearLayout) findViewById(R.id.layoutContainerAkun));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                TextView txt = v.findViewById(R.id.tInfo);
                txt.setText("TAMBAH DATA\nAKUN PENGGUNA");
                EditText etID = v.findViewById(R.id.tNIK);
                EditText etPass = v.findViewById(R.id.tPassword);
                EditText etUser = v.findViewById(R.id.tUsername);
                EditText etNama = v.findViewById(R.id.tNama);
                Spinner etLevel = v.findViewById(R.id.tLevel);
                AppCompatButton simpan = v.findViewById(R.id.user_simpan);
                AppCompatButton hapus = v.findViewById(R.id.user_hapus);
                AppCompatButton batal = v.findViewById(R.id.user_batal);
                hapus.setVisibility(View.GONE);

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nik = etID.getText().toString();
                        String nama = etNama.getText().toString();
                        String username = etUser.getText().toString();
                        String password = etPass.getText().toString();
                        int level = etLevel.getSelectedItemPosition() - 1;

                        if (nik.isEmpty()) {
                            etID.requestFocus();
                            etID.setError("Masukkan NIK");
                        } else
                        if (nama.isEmpty()) {
                            etNama.requestFocus();
                            etNama.setError("Masukkan Nama");
                        } else
                        if (username.isEmpty()) {
                            etUser.requestFocus();
                            etUser.setError("Masukkan username");
                        } else
                        if (password.isEmpty()) {
                            etPass.requestFocus();
                            etPass.setError("Masukkan password");
                        } else
                        if (level < 0) {
                            Toast.makeText(AkunPengguna.this, "Silahkan pilih level", Toast.LENGTH_SHORT).show();
                        } else {
                            modelUser model = new modelUser();
                            model.setNik(nik);
                            model.setNama(nama);
                            model.setUsername(username);
                            model.setPassword(password);
                            model.setLevel(level);
                            db.child(nik).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AkunPengguna.this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                }
                            });
                        }
                    }
                });

                if (alertDialog.getWindow()!=null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AkunPengguna.this, admin.class);
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
                mylist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelUser model = ds.getValue(modelUser.class);
                    mylist.add(model);
                }

                adapters.setList(mylist);
                listView.setAdapter(adapters);

                if (mylist.size() < 1) {
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView getID = view.findViewById(R.id.itemAkun_kode);
        final String id = getID.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(AkunPengguna.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(AkunPengguna.this).inflate(R.layout.dialog_akun, (LinearLayout) findViewById(R.id.layoutContainerAkun));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        TextView txt = v.findViewById(R.id.tInfo);
        txt.setText("UBAH DATA\nAKUN PENGGUNA");
        EditText etID = v.findViewById(R.id.tNIK);
        EditText etPass = v.findViewById(R.id.tPassword);
        EditText etUser = v.findViewById(R.id.tUsername);
        EditText etNama = v.findViewById(R.id.tNama);
        Spinner etLevel = v.findViewById(R.id.tLevel);
        AppCompatButton simpan = v.findViewById(R.id.user_simpan);
        AppCompatButton hapus = v.findViewById(R.id.user_hapus);
        AppCompatButton batal = v.findViewById(R.id.user_batal);
        etID.setFocusable(false);

        db.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelUser model = snapshot.getValue(modelUser.class);
                etID.setText(model.getNik());
                etNama.setText(model.getNama());
                etUser.setText(model.getUsername());
                etPass.setText(model.getPassword());
                int pos = model.getLevel();
                etLevel.setSelection(pos + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AkunPengguna.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Hapus Data Ini ?")
                        .setNegativeButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.child(getID.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AkunPengguna.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                            alertDialog.cancel();
                                        }
                                    }
                                });
                            }
                        })
                        .setPositiveButton("TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                alertDialog.cancel();
                            }
                        }).show();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nik = etID.getText().toString();
                String nama = etNama.getText().toString();
                String username = etUser.getText().toString();
                String password = etPass.getText().toString();
                int level = etLevel.getSelectedItemPosition() - 1;

                if (nik.isEmpty()) {
                    etID.requestFocus();
                    etID.setError("Masukkan NIK");
                } else
                if (nama.isEmpty()) {
                    etNama.requestFocus();
                    etNama.setError("Masukkan Nama");
                } else
                if (username.isEmpty()) {
                    etUser.requestFocus();
                    etUser.setError("Masukkan username");
                } else
                if (password.isEmpty()) {
                    etPass.requestFocus();
                    etPass.setError("Masukkan password");
                } else
                if (level < 0) {
                    Toast.makeText(AkunPengguna.this, "Silahkan pilih level", Toast.LENGTH_SHORT).show();
                } else {
                    db.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0) {
                                etUser.requestFocus();
                                etUser.setError("Username sudah digunakan");
                                return;
                            } else {
                                db.child(nik).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getChildrenCount() > 0) {
                                            etID.requestFocus();
                                            etID.setError("NIK sudah digunakan");
                                            return;
                                        } else {
                                            modelUser model = new modelUser();
                                            model.setNik(nik);
                                            model.setNama(nama);
                                            model.setUsername(username);
                                            model.setPassword(password);
                                            model.setLevel(level);
                                            db.child(nik).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(AkunPengguna.this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                                        alertDialog.cancel();
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

        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}