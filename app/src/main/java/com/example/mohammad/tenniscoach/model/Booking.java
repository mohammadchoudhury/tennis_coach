package com.example.mohammad.tenniscoach.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.Locale;

public class Booking extends Session {

    private String location;
    private double price;
    private DocumentReference user;
    public String coach;

    public Booking() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return "£" + String.format(Locale.UK, "%1.2f", price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }
}
