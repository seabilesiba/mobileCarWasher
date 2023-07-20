package com.example.splash.model;

public class DriverData {
    private String image,driverName,surname,phone,companyName,regNumber,driverEmail,driverPassword,uniqueId;

    public DriverData() {
    }

    public DriverData(String image, String driverName, String surname, String phone,
                      String companyName, String regNumber, String driverEmail,
                      String driverPassword, String uniqueId) {
        this.image = image;
        this.driverName = driverName;
        this.surname = surname;
        this.phone = phone;
        this.companyName = companyName;
        this.regNumber = regNumber;
        this.driverEmail = driverEmail;
        this.driverPassword = driverPassword;
        this.uniqueId = uniqueId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
