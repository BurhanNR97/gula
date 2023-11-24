package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TambahSampel extends AppCompatActivity {
    AppCompatButton simpan, keluar;
    ImageView foto;
    EditText kode, hue, sat, val;
    Spinner jenis;
    DatabaseReference db;
    StorageReference ref;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sampel);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        db = FirebaseDatabase.getInstance().getReference("sampel");
        ref = FirebaseStorage.getInstance().getReference("sampel");

        simpan = findViewById(R.id.addSampel_simpan);
        keluar = findViewById(R.id.addSampel_batal);
        kode = findViewById(R.id.addSampel_kode);
        jenis = findViewById(R.id.addSampel_jenis);
        hue = findViewById(R.id.addSampel_hue);
        sat = findViewById(R.id.addSampel_sat);
        val = findViewById(R.id.addSampel_val);
        foto = findViewById(R.id.addSampel_img);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(TambahSampel.this)
                        .galleryOnly()
                        .cropSquare()
                        .maxResultSize(200, 200)
                        .start();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jenis.getSelectedItemPosition() < 1) {
                    Snackbar.make(view, "Silahkan pilih jenis", Snackbar.LENGTH_SHORT).show();
                } else
                if (image == null) {
                    Snackbar.make(view, "Silahkan pilih gambar", Snackbar.LENGTH_SHORT).show();
                } else {
                    modelSampel model = new modelSampel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahSampel.this, R.style.AlertDialogTheme);
                    builder.setCancelable(false);
                    View v = LayoutInflater.from(TambahSampel.this).inflate(R.layout.dialog_proses,
                            (RelativeLayout) findViewById(R.id.containerLoading));
                    builder.setView(v);
                    final AlertDialog alertDialog = builder.create();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteFoto = stream.toByteArray();
                    String path = kode.getText().toString() + ".jpg";
                    UploadTask task = ref.child(path).putBytes(byteFoto);
                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot!= null) {
                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        model.setKode(kode.getText().toString());
                                        model.setJenis(jenis.getSelectedItem().toString());
                                        model.setHue(hue.getText().toString());
                                        model.setSaturation(sat.getText().toString());
                                        model.setValue(val.getText().toString());
                                        model.setUrl(task.getResult().toString());
                                        db.child(kode.getText().toString()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    alertDialog.cancel();
                                                    onBackPressed();
                                                    Toast.makeText(TambahSampel.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TambahSampel.this, Sampel.class);
        intent.putExtra("nik", getIntent().getStringExtra("nik"));
        intent.putExtra("nama", getIntent().getStringExtra("nama"));
        intent.putExtra("c", getIntent().getStringExtra("c"));
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HSV hsv = new HSV();
            hsv.Proses(image);
            hue.setText(hsv.getMeanHue() + "");
            sat.setText(hsv.getMeanSat() + "");
            val.setText(hsv.getMeanVal() + "");
            foto.setImageBitmap(image);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() < 1) {
                    kode.setText("G1001");
                } else {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        modelSampel model = ds.getValue(modelSampel.class);
                        int a = Integer.parseInt(model.getKode().substring(1)) + 1;
                        kode.setText("G" + a);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}