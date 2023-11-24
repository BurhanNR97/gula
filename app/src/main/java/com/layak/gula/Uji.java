package com.layak.gula;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Uji extends AppCompatActivity {

    Bitmap image;
    NestedScrollView layoutHitung;
    TextView teksHasil;
    DatabaseReference db;
    TextView hJ1, hJ2, pJ1, pJ2;
    TextView nK;
    ArrayList<modelSampel> listSampel;
    ImageView imgRGB, imgHSV;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uji);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_uji);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pengujian Citra");

        db = FirebaseDatabase.getInstance().getReference("rekap");
        imgRGB = findViewById(R.id.ujiRGB);
        imgHSV = findViewById(R.id.ujiHSV);
        nK = findViewById(R.id.inputK);
        listSampel = new ArrayList<>();
        layoutHitung = findViewById(R.id.lyHitung);
        teksHasil = findViewById(R.id.ujiHasil);

        hJ1 = findViewById(R.id.jenis1);
        hJ2 = findViewById(R.id.jenis2);
        pJ1 = findViewById(R.id.persen1);
        pJ2 = findViewById(R.id.persen2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Uji.this, MainActivity.class);
        intent.putExtra("nik", getIntent().getStringExtra("nik"));
        intent.putExtra("nama", getIntent().getStringExtra("nama"));
        intent.putExtra("c", getIntent().getStringExtra("c"));
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_uji, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_tambahuji) {
            if (nK.getText().toString().isEmpty()) {
                nK.requestFocus();
                nK.setError("Input nilai K");
            } else {
                int k = Integer.parseInt(nK.getText().toString());
                if (k < 1) {
                    nK.requestFocus();
                    nK.setError("K minimal 1");
                } else {
                    ImagePicker.with(Uji.this)
                            .cropSquare()
                            .maxResultSize(200, 200)
                            .start();
                }
            }
        }

        return super.onOptionsItemSelected(item);
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
            int k = Integer.parseInt(nK.getText().toString());
            hitungHSV(image, k);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sampel");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSampel.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelSampel model = ds.getValue(modelSampel.class);
                    listSampel.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hitungHSV(Bitmap image, int k) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Uji.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Uji.this).inflate(R.layout.dialog_proses,
                (RelativeLayout) findViewById(R.id.containerLoading));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String kodeUjii = simpleDateFormat.format(calendar.getTime());
        String tanggal = sdf.format(calendar.getTime());

        StorageReference ref = FirebaseStorage.getInstance().getReference("uji");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFoto = stream.toByteArray();
        String path = kodeUjii + ".jpg";
        UploadTask task = ref.child(path).putBytes(byteFoto);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot!= null) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            imgRGB.setImageBitmap(image);

                            modelRekap model = new modelRekap();
                            model.setKode(kodeUjii);
                            model.setTanggal(tanggal);
                            model.setUrl(task.getResult().toString());

                            //matriks RGB
                            int[][] mR = new int[image.getWidth()][image.getHeight()];
                            int[][] mG = new int[image.getWidth()][image.getHeight()];
                            int[][] mB = new int[image.getWidth()][image.getHeight()];

                            //matriks normalisasi RGB
                            float[][] nR = new float[image.getWidth()][image.getHeight()];
                            float[][] nG = new float[image.getWidth()][image.getHeight()];
                            float[][] nB = new float[image.getWidth()][image.getHeight()];

                            HSV hsv = new HSV();
                            hsv.Proses(image);
                            imgHSV.setImageBitmap(hsv.getImage());

                            mR = hsv.getMatriksR();
                            mG = hsv.getMatriksG();
                            mB = hsv.getMatriksB();
                            tampilRGB(mR, mG, mB);

                            nR = hsv.getNormaR();
                            nG = hsv.getNormaG();
                            nB = hsv.getNormaB();
                            tampilnorma(nR, nG, nB);

                            float h1 = hsv.getMeanHue();
                            float s1 = hsv.getMeanSat();
                            float v1 = hsv.getMeanVal();
                            tampilHSV(h1, s1, v1);
                            float[] nilaiUji = new float[3];
                            nilaiUji[0] = h1;
                            nilaiUji[1] = s1;
                            nilaiUji[2] = v1;

                            String[] kodeSampel = new String[listSampel.size()];
                            String[] jenisSampel = new String[listSampel.size()];
                            float[][] nilaiSampel = new float[listSampel.size()][3];

                            for (int i=0; i<listSampel.size(); i++) {
                                kodeSampel[i] = listSampel.get(i).getKode();
                                jenisSampel[i] = listSampel.get(i).getJenis();
                                nilaiSampel[i][0] = Float.parseFloat(listSampel.get(i).getHue());
                                nilaiSampel[i][1] = Float.parseFloat(listSampel.get(i).getSaturation());
                                nilaiSampel[i][2] = Float.parseFloat(listSampel.get(i).getValue());
                            }

                            KNN knn = new KNN(kodeSampel, jenisSampel, nilaiUji, nilaiSampel);
                            String[][] knn_hsv = knn.getHSV();

                            List<modelKNN> dataKNN = new ArrayList<>();

                            int jmlLayak = 0;
                            int jmlTidak = 0;
                            String hasill = "";
                            for (int x=0; x<k; x++) {
                                int no = x + 1;

                                if (knn_hsv[x][1].equals("Gula Rafinasi")) {
                                    jmlLayak++;
                                } else {
                                    jmlTidak++;
                                }

                                dataKNN.add(new modelKNN(no, knn_hsv[x][0], knn_hsv[x][1], koma3(Float.parseFloat(knn_hsv[x][2]))));
                            }
                            RecyclerView tampilKNN = findViewById(R.id.viewKNN);
                            AdpKNN adapterKNN = new AdpKNN(dataKNN);
                            LinearLayoutManager lm = new LinearLayoutManager(Uji.this);
                            tampilKNN.setLayoutManager(lm);
                            tampilKNN.setAdapter(adapterKNN);

                            if (jmlLayak == jmlTidak) {
                                if (knn_hsv[0][1].equals("Gula Rafinasi")) {
                                    hasill = "50% Gula Rafinasi";
                                } else {
                                    hasill = "50% Gula Non-Rafinasi";
                                }
                            } else {
                                float nilaiK = k * 1f;
                                float hasilLayak = (jmlLayak / nilaiK) * 100f;
                                float hasilTidak = (jmlTidak / nilaiK) * 100f;

                                float target = Float.parseFloat(knn_hsv[0][2]);

                                if (target <= 1.00000) {
                                    if (jmlLayak > jmlTidak) {
                                        model.setHasil("Gula Rafinasi");
                                        model.setPersen(hasilLayak);
                                        hJ1.setText("Gula Rafinasi");
                                        pJ1.setText(koma2(hasilLayak) + " %");
                                        hJ2.setText("Gula Non-Rafinasi");
                                        pJ2.setText(koma2(hasilTidak) + " %");
                                        hasill = koma2(hasilLayak) + "% Gula Rafinasi";
                                    } else
                                    if (jmlLayak < jmlTidak) {
                                        model.setHasil("Gula Non-Rafinasi");
                                        model.setPersen(hasilTidak);
                                        hJ2.setText("Gula Rafinasi");
                                        pJ2.setText(koma2(hasilLayak) + " %");
                                        hJ1.setText("Gula Non-Rafinasi");
                                        pJ1.setText(koma2(hasilTidak) + " %");
                                        hasill = koma2(hasilTidak) + "% Gula Non-Rafinasi";
                                    }
                                    teksHasil.setText(hasill);
                                } else {
                                    model.setHasil("Bukan Citra Gula");
                                    teksHasil.setText("Bukan Citra Gula");
                                }
                            }
                            model.setNik(getIntent().getStringExtra("nik"));
                            model.setNama(getIntent().getStringExtra("nama"));
                            db.child(kodeUjii).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        alertDialog.cancel();
                                        layoutHitung.setVisibility(View.VISIBLE);
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

    private void tampilRGB(int[][] mR, int[][] mG, int[][] mB) {
        TextView b11 = findViewById(R.id.rgb_11); TextView b12 = findViewById(R.id.rgb_12); TextView b13 = findViewById(R.id.rgb_13); TextView b1n = findViewById(R.id.rgb_1n);
        TextView b21 = findViewById(R.id.rgb_21); TextView b22 = findViewById(R.id.rgb_22); TextView b23 = findViewById(R.id.rgb_23); TextView b2n = findViewById(R.id.rgb_2n);
        TextView b31 = findViewById(R.id.rgb_31); TextView b32 = findViewById(R.id.rgb_32); TextView b33 = findViewById(R.id.rgb_33); TextView b3n = findViewById(R.id.rgb_3n);
        TextView bn1 = findViewById(R.id.rgb_n1); TextView bn2 = findViewById(R.id.rgb_n2); TextView bn3 = findViewById(R.id.rgb_n3); TextView bnn = findViewById(R.id.rgb_nn);

        b11.setText("R : " + mR[0][0] + "\n" + "G : " + mG[0][0] + "\n" + "B : " + mB[0][0]);
        b12.setText("R : " + mR[0][1] + "\n" + "G : " + mG[0][1] + "\n" + "B : " + mB[0][1]);
        b13.setText("R : " + mR[0][2] + "\n" + "G : " + mG[0][2] + "\n" + "B : " + mB[0][2]);
        b1n.setText("R : " + mR[0][mR.length - 1] + "\n" + "G : " + mG[0][mG.length -1] + "\n" + "B : " + mB[0][mG.length - 1]);

        b21.setText("R : " + mR[1][0] + "\n" + "G : " + mG[1][0] + "\n" + "B : " + mB[1][0]);
        b22.setText("R : " + mR[1][1] + "\n" + "G : " + mG[1][1] + "\n" + "B : " + mB[1][1]);
        b23.setText("R : " + mR[1][2] + "\n" + "G : " + mG[1][2] + "\n" + "B : " + mB[1][2]);
        b2n.setText("R : " + mR[1][mR.length - 1] + "\n" + "G : " + mG[1][mG.length -1] + "\n" + "B : " + mB[1][mG.length - 1]);

        b31.setText("R : " + mR[2][0] + "\n" + "G : " + mG[2][0] + "\n" + "B : " + mB[2][0]);
        b32.setText("R : " + mR[2][1] + "\n" + "G : " + mG[2][1] + "\n" + "B : " + mB[2][1]);
        b33.setText("R : " + mR[2][2] + "\n" + "G : " + mG[2][2] + "\n" + "B : " + mB[2][2]);
        b3n.setText("R : " + mR[2][mR.length - 1] + "\n" + "G : " + mG[2][mG.length -1] + "\n" + "B : " + mB[2][mG.length - 1]);

        bn1.setText("R : " + mR[mR.length - 1][0] + "\n" + "G : " + mG[mG.length - 1][0] + "\n" + "B : " + mB[mB.length - 1][0]);
        bn2.setText("R : " + mR[mR.length - 1][1] + "\n" + "G : " + mG[mG.length - 1][1] + "\n" + "B : " + mB[mB.length - 1][1]);
        bn3.setText("R : " + mR[mR.length - 1][2] + "\n" + "G : " + mG[mG.length - 1][2] + "\n" + "B : " + mB[mB.length - 1][2]);
        bnn.setText("R : " + mR[mR.length - 1][mR.length - 1] + "\n" + "G : " + mG[mG.length - 1][mG.length -1] + "\n" + "B : " + mB[mB.length - 1][mG.length - 1]);
    }

    private void tampilnorma(float[][] nR, float[][] nG, float[][] nB) {
        TextView b11 = findViewById(R.id.norma_11); TextView b12 = findViewById(R.id.norma_12); TextView b13 = findViewById(R.id.norma_13); TextView b1n = findViewById(R.id.norma_1n);
        TextView b21 = findViewById(R.id.norma_21); TextView b22 = findViewById(R.id.norma_22); TextView b23 = findViewById(R.id.norma_23); TextView b2n = findViewById(R.id.norma_2n);
        TextView b31 = findViewById(R.id.norma_31); TextView b32 = findViewById(R.id.norma_32); TextView b33 = findViewById(R.id.norma_33); TextView b3n = findViewById(R.id.norma_3n);
        TextView bn1 = findViewById(R.id.norma_n1); TextView bn2 = findViewById(R.id.norma_n2); TextView bn3 = findViewById(R.id.norma_n3); TextView bnn = findViewById(R.id.norma_nn);

        b11.setText("R : " + koma3(nR[0][0]) + "\n" + "G : " + koma3(nG[0][0]) + "\n" + "B : " + koma3(nB[0][0]));
        b12.setText("R : " + koma3(nR[0][1]) + "\n" + "G : " + koma3(nG[0][1]) + "\n" + "B : " + koma3(nB[0][1]));
        b13.setText("R : " + koma3(nR[0][2]) + "\n" + "G : " + koma3(nG[0][2]) + "\n" + "B : " + koma3(nB[0][2]));
        b1n.setText("R : " + koma3(nR[0][nR.length - 1]) + "\n" + "G : " + koma3(nG[0][nG.length -1]) + "\n" + "B : " + koma3(nB[0][nG.length - 1]));

        b21.setText("R : " + koma3(nR[1][0]) + "\n" + "G : " + koma3(nG[1][0]) + "\n" + "B : " + koma3(nB[1][0]));
        b22.setText("R : " + koma3(nR[1][1]) + "\n" + "G : " + koma3(nG[1][1]) + "\n" + "B : " + koma3(nB[1][1]));
        b23.setText("R : " + koma3(nR[1][2]) + "\n" + "G : " + koma3(nG[1][2]) + "\n" + "B : " + koma3(nB[1][2]));
        b2n.setText("R : " + koma3(nR[1][nR.length - 1]) + "\n" + "G : " + koma3(nG[1][nG.length -1]) + "\n" + "B : " + koma3(nB[1][nG.length - 1]));

        b31.setText("R : " + koma3(nR[2][0]) + "\n" + "G : " + koma3(nG[2][0]) + "\n" + "B : " + koma3(nB[2][0]));
        b32.setText("R : " + koma3(nR[2][1]) + "\n" + "G : " + koma3(nG[2][1]) + "\n" + "B : " + koma3(nB[2][1]));
        b33.setText("R : " + koma3(nR[2][2]) + "\n" + "G : " + koma3(nG[2][2]) + "\n" + "B : " + koma3(nB[2][2]));
        b3n.setText("R : " + koma3(nR[2][nR.length - 1]) + "\n" + "G : " + koma3(nG[2][nG.length -1]) + "\n" + "B : " + koma3(nB[2][nG.length - 1]));

        bn1.setText("R : " + koma3(nR[nR.length - 1][0]) + "\n" + "G : " + koma3(nG[nG.length - 1][0]) + "\n" + "B : " + koma3(nB[nB.length - 1][0]));
        bn2.setText("R : " + koma3(nR[nR.length - 1][1]) + "\n" + "G : " + koma3(nG[nG.length - 1][1]) + "\n" + "B : " + koma3(nB[nB.length - 1][1]));
        bn3.setText("R : " + koma3(nR[nR.length - 1][2]) + "\n" + "G : " + koma3(nG[nG.length - 1][2]) + "\n" + "B : " + koma3(nB[nB.length - 1][2]));
        bnn.setText("R : " + koma3(nR[nR.length - 1][nR.length - 1]) + "\n" + "G : " + koma3(nG[nG.length - 1][nG.length -1]) + "\n" + "B : " + koma3(nB[nB.length - 1][nG.length - 1]));
    }

    private String koma3(float nilai) {
        String a = String.valueOf(nilai).replace(",",".");
        double b = Float.parseFloat(a);
        return String.format("%.3f", b);
    }

    private String koma2(float nilai) {
        String a = String.valueOf(nilai).replace(",",".");
        double b = Float.parseFloat(a);
        return String.format("%.2f", b);
    }

    private void tampilHSV(float H, float S, float V) {
        TextView h = findViewById(R.id.hsv_hue);
        TextView s = findViewById(R.id.hsv_saturation);
        TextView v = findViewById(R.id.hsv_value);

        h.setText(koma3(H));
        s.setText(koma3(S));
        v.setText(koma3(V));
    }
}