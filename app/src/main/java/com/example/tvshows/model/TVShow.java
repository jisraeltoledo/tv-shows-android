package com.example.tvshows.model;

import android.provider.MediaStore;

import java.io.Serializable;

public class TVShow implements Serializable {
    private int id;
    private Images image;
    private String name;
    private String summary;
    private Externals externals;

    public String getPoster() {
        return image.getMedium();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Externals getExternals() {
        return externals;
    }

    public void setExternals(Externals externals) {
        this.externals = externals;
    }

    public String getImdb (){
        return externals.getImdb();
    }

    @Override
    public String toString() {
        return name;
    }

}

class Images implements Serializable{
    private String medium;
    private String original;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}

class Externals implements Serializable{
    private String imdb;

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}