package com.example.splash.model;

public class ChatData {
    private String message,date,driverId,clientId,key,key1;

    public ChatData() {
    }

    public ChatData(String message, String date, String driverId, String clientId, String key) {
        this.message = message;
        this.date = date;
        this.driverId = driverId;
        this.clientId = clientId;
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
