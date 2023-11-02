package com.layak.gula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adpSampel extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelSampel> list = new ArrayList<>();

    public void setList(ArrayList<modelSampel> list) {
        this.list = list;
    }

    public adpSampel(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_sampel, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelSampel model = (modelSampel) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, jenis, hue, value, saturation;
        ImageView img;

        ViewHolder(View view) {
            img = view.findViewById(R.id.itemSampel_foto);
            kode = view.findViewById(R.id.itemSampel_kode);
            hue = view.findViewById(R.id.itemSampel_hue);
            jenis = view.findViewById(R.id.itemSampel_jenis);
            value = view.findViewById(R.id.itemSampel_value);
            saturation = view.findViewById(R.id.itemSampel_saturation);
        }

        void bind(modelSampel model) {
            kode.setText(model.getKode());
            jenis.setText(model.getJenis());
            hue.setText(model.getHue());
            saturation.setText(model.getSaturation());
            value.setText(model.getValue());
            Glide.with(mContext).load(model.getUrl()).into(img);
        }
    }
}
