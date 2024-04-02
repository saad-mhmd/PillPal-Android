package com.example.pillpal_v2.Model;

import android.graphics.drawable.Drawable;

public class FindMedConstructor {

FindMedConstructor(){}
    private String PharmaName, place, price,phoneNN;

    public String getPhoneNN() {
        return phoneNN;
    }

    public void setPhoneNN(String phoneNN) {
        this.phoneNN = phoneNN;
    }

    public String getPharmaName() {
        return PharmaName;
    }

    public void setPharmaName(String pharmaName) {
        PharmaName = pharmaName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
