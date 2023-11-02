package com.layak.gula;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

public class HSV {
    ArrayList<Float> hue, saturation, value;
    Bitmap image;
    float meanHue, meanSat, meanVal;
    int[][] matriksR;
    int[][] matriksG;
    int[][] matriksB;

    float[][] normaR;
    float[][] normaG;
    float[][] normaB;

    public HSV() {}

    public void Proses(Bitmap bitmap) {
        hue = new ArrayList<>();
        saturation = new ArrayList<>();
        value = new ArrayList<>();

        Bitmap bmp = IMGtoHSV(bitmap);
        setImage(bmp);
        setMeanHue(meanH(hue));
        setMeanSat(meanS(saturation));
        setMeanVal(meanV(value));
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getMeanHue() {
        return meanHue;
    }

    public void setMeanHue(float meanHue) {
        this.meanHue = meanHue;
    }

    public float getMeanSat() {
        return meanSat;
    }

    public void setMeanSat(float meanSat) {
        this.meanSat = meanSat;
    }

    public float getMeanVal() {
        return meanVal;
    }

    public void setMeanVal(float meanVal) {
        this.meanVal = meanVal;
    }

    public int[][] getMatriksR() {
        return matriksR;
    }

    public void setMatriksR(int[][] matriksR) {
        this.matriksR = matriksR;
    }

    public int[][] getMatriksG() {
        return matriksG;
    }

    public void setMatriksG(int[][] matriksG) {
        this.matriksG = matriksG;
    }

    public int[][] getMatriksB() {
        return matriksB;
    }

    public void setMatriksB(int[][] matriksB) {
        this.matriksB = matriksB;
    }

    public float[][] getNormaR() {
        return normaR;
    }

    public void setNormaR(float[][] normaR) {
        this.normaR = normaR;
    }

    public float[][] getNormaG() {
        return normaG;
    }

    public void setNormaG(float[][] normaG) {
        this.normaG = normaG;
    }

    public float[][] getNormaB() {
        return normaB;
    }

    public void setNormaB(float[][] normaB) {
        this.normaB = normaB;
    }

    private Bitmap IMGtoHSV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        matriksR = new int[width][height];
        matriksG = new int[width][height];
        matriksB = new int[width][height];

        normaR = new float[width][height];
        normaG = new float[width][height];
        normaB = new float[width][height];

        Bitmap img = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int R, G, B;
        int piksel;
        for (int x=0; x<width; x++) {
            for (int y = 0; y < height; y++) {
                piksel = bitmap.getPixel(x, y);
                R = Color.red(piksel);
                G = Color.green(piksel);
                B = Color.blue(piksel);

                //mengambil matriks R G B
                matriksR[x][y] = R;
                matriksG[x][y] = G;
                matriksB[x][y] = B;

                //Menghitung normalisasi matriks R G B
                float total = R + G + B;
                float r = R / total;
                float g = G / total;
                float b = B / total;

                //mengambil matriks normalisasi R G B
                normaR[x][y] = r;
                normaG[x][y] = g;
                normaB[x][y] = b;

                float[] hsv = RGBtoHSV(r, g, b);
                hue.add(hsv[0]); saturation.add(hsv[1]); value.add(hsv[2]);
                img.setPixel(x, y, Color.HSVToColor(hsv));
            }
        }
        return img;
    }

    private static float[] RGBtoHSV(float r, float g, float b){
        float[] hsv = new float[3];


        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));

        //Value
        hsv[2] = max;

        //Saturation
        if (hsv[2] == 0) {
            hsv[1] = 0;
        } else
        if (hsv[2] > 0) {
            hsv[1] = 1f - (min / hsv[2]);
        }

        //Hue
        if (hsv[1] == 0) {
            hsv[0] = 0;
        } else
        if (max == r) {
            hsv[0] = (60f * (g - b)) / (hsv[1] * hsv[2]);
        } else
        if (max == g) {
            hsv[0] = 60f * (2f + ((b - r) / ((hsv[1] * hsv[2]))));
        } else
        if (max == b) {
            hsv[0] = 60f * (4f + ((r - g) / ((hsv[1] * hsv[2]))));
        }

        if (hsv[0] < 0) {
            hsv[0] = hsv[0] + 360f;
        }

        return hsv;
    }

    private float meanH(ArrayList<Float> h) {
        float sum = 0;
        for (int i=0; i<h.size(); ++i) {
            sum = sum + h.get(i);
        }
        return sum/((float) (image.getWidth() * image.getHeight()));
    }

    private float meanS(ArrayList<Float> s) {
        float sum = 0;
        for (int i=0; i<s.size(); ++i) {
            sum = sum + s.get(i);
        }
        return sum/((float) (image.getWidth() * image.getHeight()));
    }

    private float meanV(ArrayList<Float> v) {
        float sum = 0;
        for (int i=0; i<v.size(); ++i) {
            sum = sum + v.get(i);
        }
        return sum/((float) (image.getWidth() * image.getHeight()));
    }
}
