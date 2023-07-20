package com.example.splash.model;

public class AcceptedData {
    private String uniqueId,image,name,surname,number,email,location,status,date;

    private int price;
    private double latitude,longitude;

    public AcceptedData() {
    }

    public AcceptedData(String uniqueId, String image, String name, String surname, String number,
                        String email, String location, String status, String date,
                        int price, double latitude, double longitude) {
        this.uniqueId = uniqueId;
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.email = email;
        this.location = location;
        this.status = status;
        this.date = date;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
