package com.example.mohammad.tenniscoach.model;

import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {

    private DocumentReference from;
    private String message;
    private Date date;
    private DocumentReference to;

    public Message() {
    }

    public DocumentReference getFrom() {
        return from;
    }

    public void setFrom(DocumentReference from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM yy HH:mm", Locale.UK);
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DocumentReference getTo() {
        return to;
    }

    public void setTo(DocumentReference to) {
        this.to = to;
    }
}
