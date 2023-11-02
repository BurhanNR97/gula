package com.layak.gula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adpRekap extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelRekap> list = new ArrayList<>();

    public void setList(ArrayList<modelRekap> list) {
        this.list = list;
    }

    public adpRekap(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rekap, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelRekap model = (modelRekap) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, nik, nama, hasil, tanggal;
        ImageView img;

        ViewHolder(View view) {
            img = view.findViewById(R.id.itemUji_foto);
            kode = view.findViewById(R.id.itemUji_kode);
            nik = view.findViewById(R.id.itemUji_nik);
            nama = view.findViewById(R.id.itemUji_nama);
            hasil = view.findViewById(R.id.itemUji_hasil);
            tanggal = view.findViewById(R.id.itemUji_tanggal);
        }

        void bind(modelRekap model) {
            kode.setText(model.getKode());
            nik.setText(model.getNik());
            nama.setText(model.getNama());
            if (model.getHasil().equals("Bukan Citra Gula")) {
                hasil.setText("Bukan Citra Gula");
            } else {
                hasil.setText(koma2(model.getPersen()) + "% " + model.getHasil());
            }
            tanggal.setText(model.getTanggal());
            Glide.with(mContext).load(model.getUrl()).into(img);
        }
    }

    private String koma2(double nilai) {
        String a = String.valueOf(nilai).replace(",",".");
        double b = Float.parseFloat(a);
        return String.format("%.2f", b);
    }
}
