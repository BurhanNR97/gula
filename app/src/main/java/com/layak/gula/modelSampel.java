package com.layak.gula;

import android.os.Parcel;
import android.os.Parcelable;

public class modelSampel implements Parcelable {

    private String kode;
    private String jenis;
    private String hue;
    private String saturation;
    private String value;
    private String url;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getHue() {
        return hue;
    }

    public void setHue(String hue) {
        this.hue = hue;
    }

    public String getSaturation() {
        return saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public modelSampel() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode);
        dest.writeString(this.hue);
        dest.writeString(this.jenis);
        dest.writeString(this.value);
        dest.writeString(this.saturation);
        dest.writeString(this.url);
    }

    protected modelSampel(Parcel in) {
        this.kode = in.readString();
        this.jenis = in.readString();
        this.hue = in.readString();
        this.saturation = in.readString();
        this.value = in.readString();
        this.url = in.readString();
    }

    public static final Creator<modelSampel> CREATOR = new Creator<modelSampel>() {
        @Override
        public modelSampel createFromParcel(Parcel parcel) {
            return new modelSampel(parcel);
        }

        @Override
        public modelSampel[] newArray(int i) {
            return new modelSampel[i];
        }
    };
}
