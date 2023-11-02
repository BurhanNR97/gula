package com.layak.gula;

public class modelNorma {
    private int no;
    private String satu;
    private String dua;
    private String tiga;
    private String titik;
    private String terakhir;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getSatu() {
        return satu;
    }

    public void setSatu(String satu) {
        this.satu = satu;
    }

    public String getDua() {
        return dua;
    }

    public void setDua(String dua) {
        this.dua = dua;
    }

    public String getTiga() {
        return tiga;
    }

    public void setTiga(String tiga) {
        this.tiga = tiga;
    }

    public String getTitik() {
        return titik;
    }

    public void setTitik(String titik) {
        this.titik = titik;
    }

    public String getTerakhir() {
        return terakhir;
    }

    public void setTerakhir(String terakhir) {
        this.terakhir = terakhir;
    }

    public modelNorma(int no, String satu, String dua, String tiga, String titik, String terakhir) {
        this.no = no;
        this.satu = satu;
        this.dua = dua;
        this.tiga = tiga;
        this.titik = titik;
        this.terakhir = terakhir;
    }
}
