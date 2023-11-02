package com.layak.gula;

import android.os.Parcel;
import android.os.Parcelable;

public class modelUser implements Parcelable {

    private String nik;
    private String nama;
    private String username;
    private String password;
    private int level;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public modelUser() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nik);
        dest.writeString(this.nama);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeInt(this.level);
    }

    protected modelUser(Parcel in) {
        this.nik = in.readString();
        this.nama = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.level = in.readInt();
    }

    public static final Creator<modelUser> CREATOR = new Creator<modelUser>() {
        @Override
        public modelUser createFromParcel(Parcel parcel) {
            return new modelUser(parcel);
        }

        @Override
        public modelUser[] newArray(int i) {
            return new modelUser[i];
        }
    };
}
