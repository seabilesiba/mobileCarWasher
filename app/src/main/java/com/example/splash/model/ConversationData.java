package com.example.splash.model;

public class ConversationData {
    private String convID,message,date,driverId,clientId,key;

    public ConversationData() {
    }

    public ConversationData(String convID, String message, String date, String driverId, String clientId, String key) {
        this.convID = convID;
        this.message = message;
        this.date = date;
        this.driverId = driverId;
        this.clientId = clientId;
        this.key = key;
    }

    public String getConvID() {
        return convID;
    }

    public void setConvID(String convID) {
        this.convID = convID;
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
