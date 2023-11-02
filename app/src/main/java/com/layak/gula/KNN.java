package com.layak.gula;

public class KNN {
    private float[] uji;
    private float[][] latih;
    private String[] kualitas;
    private String[] kode;
    private String[][] HSV;
    private String Hasil;
    private float Persen;

    public KNN(String[] kode, String[] kualitas, float[] uji, float[][] latih) {
        this.kode = kode;
        this.kualitas = kualitas;
        this.uji = uji;
        this.latih = latih;

        float[] HSV = distance(uji, latih);
        this.HSV = hsv(kode, kualitas, HSV);
    }

    private float[] distance(float[] data1, float[][] data2) {
        float[] hasil = new float[data2.length];
        for (int i=0; i<data2.length; i++) {
            float hue = (float) (Math.pow(data2[i][0] - data1[0], 2));
            float sat = (float) (Math.pow(data2[i][1] - data1[1], 2));
            float val = (float) (Math.pow(data2[i][2] - data1[2], 2));
            float total = hue + sat + val;
            hasil[i] = (float) Math.sqrt(total);
        }
        return hasil;
    }

    private String[][] hsv (String[] id, String[] jenis, float[] nilai) {
        String[] hasilKode = new String[id.length];
        String[] hasilJenis = new String[jenis.length];
        float[] hasilNilai = new float[nilai.length];
        String[][] hasil = new String[nilai.length][3];


        for (int k=0; k<nilai.length; k++) {
            hasilKode[k] = id[k];
            hasilJenis[k] = jenis[k];
            hasilNilai[k] = nilai[k];
        }

        for (int x=0; x<hasilNilai.length; x++) {
            for (int y=x+1; y<hasilNilai.length; y++) {
                if (hasilNilai[x] > hasilNilai[y]) {
                    float tmpNilai = hasilNilai[x];
                    hasilNilai[x] = hasilNilai[y];
                    hasilNilai[y] = tmpNilai;

                    String tmpKode = hasilKode[x];
                    hasilKode[x] = hasilKode[y];
                    hasilKode[y] = tmpKode;

                    String tmpJenis = hasilJenis[x];
                    hasilJenis[x] = hasilJenis[y];
                    hasilJenis[y] = tmpJenis;
                }
            }
        }

        for (int z=0; z<hasilNilai.length; z++) {
            hasil[z][0] = hasilKode[z];
            hasil[z][1] = hasilJenis[z];
            hasil[z][2] = Float.toString(hasilNilai[z]);
        }

        return hasil;
    }

    public String[][] getHSV() {
        return HSV;
    }

    public void setHSV(String[][] HSV) {
        this.HSV = HSV;
    }

    public String getHasil() {
        return Hasil;
    }

    public void setHasil(String hasil) {
        Hasil = hasil;
    }

    public float getPersen() {
        return Persen;
    }

    public void setPersen(float persen) {
        Persen = persen;
    }
}
