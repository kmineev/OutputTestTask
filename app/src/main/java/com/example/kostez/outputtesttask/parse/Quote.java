package com.example.kostez.outputtesttask.parse;

/**
 * Created by Kostez on 14.09.2016.
 */
public class Quote {

    private final int id;
    private final String date;
    private final String text;

    public Quote(int id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
