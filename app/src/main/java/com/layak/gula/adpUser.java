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

public class adpUser extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelUser> list = new ArrayList<>();

    public void setList(ArrayList<modelUser> list) {
        this.list = list;
    }

    public adpUser(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_akun, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelUser model = (modelUser) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, nama, level, username, password;

        ViewHolder(View view) {
            kode = view.findViewById(R.id.itemAkun_kode);
            nama = view.findViewById(R.id.itemAkun_nama);
            level = view.findViewById(R.id.itemAkun_level);
            username = view.findViewById(R.id.itemAkun_username);
            password = view.findViewById(R.id.itemAkun_pass);
        }

        void bind(modelUser model) {
            kode.setText(model.getNik());
            nama.setText(model.getNama());
            if (model.getLevel() == 0) {
                level.setText("Administrator");
            } else
            if (model.getLevel() == 1) {
                level.setText("Penguji");
            }
            username.setText(model.getUsername());
            password.setText(model.getPassword());
        }
    }
}
