package com.example.splash.model;

public class OwnerData {
    private String key, image, CompanyName, RegNumber, OwnerEmail, OwnerPhone, OwnerPassword;

    public OwnerData() {
    }

    public OwnerData(String key, String image, String companyName, String regNumber,
                     String ownerEmail, String ownerPhone, String ownerPassword) {
        this.key = key;
        this.image = image;
        CompanyName = companyName;
        RegNumber = regNumber;
        OwnerEmail = ownerEmail;
        OwnerPhone = ownerPhone;
        OwnerPassword = ownerPassword;
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

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getRegNumber() {
        return RegNumber;
    }

    public void setRegNumber(String regNumber) {
        RegNumber = regNumber;
    }

    public String getOwnerEmail() {
        return OwnerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        OwnerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return OwnerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        OwnerPhone = ownerPhone;
    }

    public String getOwnerPassword() {
        return OwnerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        OwnerPassword = ownerPassword;
    }
}
