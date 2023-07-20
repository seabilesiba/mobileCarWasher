package com.example.splash.model;

public class CarData {
    private String key,image,title;
    private int price;

    public CarData() {
    }

    public CarData(String key, String image, String title, int price) {
        this.key = key;
        this.image = image;
        this.title = title;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
