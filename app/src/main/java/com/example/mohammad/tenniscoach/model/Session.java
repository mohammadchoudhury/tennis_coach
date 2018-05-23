package com.example.mohammad.tenniscoach.model;

import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Session {

    private String ID;
    private String type;
    private Date date;
    private double price;

    public Session() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM yyyy", Locale.UK);
        return sdf.format(date);
    }

    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        Date d = new Date();
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = d;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
