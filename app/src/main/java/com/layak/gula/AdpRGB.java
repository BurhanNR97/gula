package com.layak.gula;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpRGB extends RecyclerView.Adapter {

    private List<modelRGB> dataList;

    public AdpRGB(List<modelRGB> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabel_rgb, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.no.setBackgroundResource(R.drawable.border_header);
            rowViewHolder.satu.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.dua.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.tiga.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.titik.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.terakhir.setBackgroundResource(R.drawable.border_right);

            rowViewHolder.no.setTypeface(null, Typeface.BOLD);
            rowViewHolder.satu.setTypeface(null, Typeface.BOLD);
            rowViewHolder.dua.setTypeface(null, Typeface.BOLD);
            rowViewHolder.tiga.setTypeface(null, Typeface.BOLD);
            rowViewHolder.titik.setTypeface(null, Typeface.BOLD);
            rowViewHolder.terakhir.setTypeface(null, Typeface.BOLD);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.satu.setTextSize(18f);
            rowViewHolder.dua.setTextSize(18f);
            rowViewHolder.tiga.setTextSize(18f);
            rowViewHolder.titik.setTextSize(18f);
            rowViewHolder.terakhir.setTextSize(18f);

            rowViewHolder.no.setText("No");
            rowViewHolder.satu.setText("1");
            rowViewHolder.dua.setText("2");
            rowViewHolder.tiga.setText("3");
            rowViewHolder.titik.setText("...");
            rowViewHolder.terakhir.setText("200");
        } else {
            modelRGB data = dataList.get(rowPos - 1);

            rowViewHolder.no.setBackgroundResource(R.drawable.border_field);
            rowViewHolder.satu.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.dua.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.tiga.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.titik.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.terakhir.setBackgroundResource(R.drawable.border_bottom);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.satu.setTextSize(18f);
            rowViewHolder.dua.setTextSize(18f);
            rowViewHolder.tiga.setTextSize(18f);
            rowViewHolder.titik.setTextSize(18f);
            rowViewHolder.terakhir.setTextSize(18f);

            rowViewHolder.no.setText(data.getNo() + "");
            rowViewHolder.satu.setText(data.getSatu() + "");
            rowViewHolder.dua.setText(data.getDua() + "");
            rowViewHolder.tiga.setText(data.getTiga() + "");
            rowViewHolder.titik.setText("...");
            rowViewHolder.terakhir.setText(data.getTerakhir() + "");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView no;
        TextView satu;
        TextView dua;
        TextView tiga;
        TextView titik;
        TextView terakhir;

        RowViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.rgb_no);
            satu = itemView.findViewById(R.id.rgb_satu);
            dua = itemView.findViewById(R.id.rgb_dua);
            tiga = itemView.findViewById(R.id.rgb_tiga);
            titik = itemView.findViewById(R.id.rgb_titik);
            terakhir = itemView.findViewById(R.id.rgb_terakhir);
        }
    }
}
