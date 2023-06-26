package com.example.splash.model;

public class AcceptedData {
    private String uniqueId,image,name,surname,location;
    private double latitude,longitude;

    public AcceptedData() {
    }

    public AcceptedData(String uniqueId, String image, String name, String surname,
                       String location, double latitude, double longitude) {
        this.uniqueId = uniqueId;
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
