package com.example.zzy4f5da2.mysqlconntest;

import android.graphics.Bitmap;

/**
 * Created by Akari on 2018/7/6.
 */

public class Data {
    String content;
    String date;
    int color;
    Bitmap img;
    int SEX;
    int nice;

    public Data(String content, String date, int color, int SEX, int nice) {
        this.content = content;
        this.date = date;
        this.color = color;
        this.SEX = SEX;
        this.nice = nice;
    }

    public Data(String content, String date, int color, Bitmap img, int SEX, int nice) {
        this.content = content;
        this.date = date;
        this.color = color;
        this.img = img;
        this.SEX = SEX;
        this.nice = nice;
    }

    public int getNice() {
        return nice;
    }

    public void setNice(int nice) {
        this.nice = nice;
    }

    public Data(int SEX) {
        this.SEX = SEX;
    }

    public int getSEX() {
        return SEX;
    }

    public void setSEX(int SEX) {
        this.SEX = SEX;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
