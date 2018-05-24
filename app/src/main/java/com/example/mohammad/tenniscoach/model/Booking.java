package com.example.mohammad.tenniscoach.model;

import com.google.firebase.firestore.DocumentReference;

public class Booking extends Session {

    private String court;
    private DocumentReference user;
    public String coach;

    public Booking() {
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String location) {
        this.court = location;
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
