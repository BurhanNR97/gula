package com.layak.gula;

public class modelKNN {
    private int no;
    private String kode;
    private String kualitas;
    private String nilai;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKualitas() {
        return kualitas;
    }

    public void setKualitas(String kualitas) {
        this.kualitas = kualitas;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public modelKNN(int no, String kode, String kualitas, String nilai) {
        this.no = no;
        this.kode = kode;
        this.kualitas = kualitas;
        this.nilai = nilai;
    }
}
