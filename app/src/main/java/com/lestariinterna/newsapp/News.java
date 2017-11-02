package com.lestariinterna.newsapp;

import android.graphics.Bitmap;

/**
 * Created by AllinOne on 21/10/2017.
 */

public class News {

    private String title;
    private String section;
    private String date;
    private String author;
    private Bitmap thumbnail;
    private String artikelUrl;

    public News(String title,String section, String date,String artikelUrl,String author,Bitmap thumbnail){
        this.title = title;
        this.section = section;
        this.date = date;
        this.author = author;
        this.thumbnail = thumbnail;
        this.artikelUrl = artikelUrl;
    }


    public String getTitle(){return title;}
    public String getAuthor() {
        return author;
    }

    public String getArtikelUrl() {
        return artikelUrl;
    }
    public String getSection(){
        return section;
    }
    public String getDate(){
        return date;
    }
    public Bitmap getThumbnail(){
        return thumbnail;
    }
}
