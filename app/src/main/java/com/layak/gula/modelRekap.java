package com.layak.gula;

import android.os.Parcel;
import android.os.Parcelable;

public class modelRekap implements Parcelable {

    private String kode;
    private String nik;
    private String nama;
    private String hasil;
    private double persen;
    private String tanggal;
    private String url;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public double getPersen() {
        return persen;
    }

    public void setPersen(double persen) {
        this.persen = persen;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public modelRekap() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode);
        dest.writeString(this.nik);
        dest.writeString(this.nama);
        dest.writeString(this.hasil);
        dest.writeDouble(this.persen);
        dest.writeString(this.tanggal);
        dest.writeString(this.url);
    }

    protected modelRekap(Parcel in) {
        this.kode = in.readString();
        this.nik = in.readString();
        this.nama = in.readString();
        this.hasil = in.readString();
        this.persen = in.readDouble();
        this.tanggal = in.readString();
        this.url = in.readString();
    }

    public static final Creator<modelRekap> CREATOR = new Creator<modelRekap>() {
        @Override
        public modelRekap createFromParcel(Parcel parcel) {
            return new modelRekap(parcel);
        }

        @Override
        public modelRekap[] newArray(int i) {
            return new modelRekap[i];
        }
    };
}
