package com.lestariinterna.newsapp;

/**
 * Created by AllinOne on 21/10/2017.
 */

public class News {

    private String title;
    private String section;
    private String date;
    private String author;
    private String thumbnail;
    private String artikelUrl;

    public News(String title,String section, String date,String artikelUrl,String author,String thumbnail){
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
    public String getThumbnail(){return thumbnail;}
}
