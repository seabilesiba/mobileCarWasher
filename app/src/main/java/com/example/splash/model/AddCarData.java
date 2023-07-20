package com.example.splash.model;


public class AddCarData {
    private String key, image, title, numberP;

    public AddCarData() {
    }

    public AddCarData(String key, String image, String title, String numberP) {
        this.key = key;
        this.image = image;
        this.title = title;
        this.numberP = numberP;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumberP() {
        return numberP;
    }

    public void setNumberP(String numberP) {
        this.numberP = numberP;
    }
}