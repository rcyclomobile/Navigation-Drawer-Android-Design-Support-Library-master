package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

/**
 * Created by Roberto on 18-12-2015.
 */
public class Establishment {

    String name;
    String email;
    String password;
    String phone;
    String address;
    String waste;
    String activo;

    //Constructor
    public Establishment(String name, String email, String password, String phone, String address, String waste, String activo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.waste = waste;
        this.activo = activo;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWaste() {
        return waste;
    }

    public void setWaste(String waste) {
        this.waste = waste;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }


}
