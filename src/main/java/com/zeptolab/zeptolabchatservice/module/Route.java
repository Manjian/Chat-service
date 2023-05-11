package com.zeptolab.zeptolabchatservice.module;

public enum Route {


    LOGIN("/login"),
    JOIN("/join"),
    LEAVE("/leave"),
    DISCONNECT("/disconnect"),
    LIST("/list"),
    USER("/users");


    private final String stringValue;


    Route(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }


}
