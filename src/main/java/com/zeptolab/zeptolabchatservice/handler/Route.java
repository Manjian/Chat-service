package com.zeptolab.zeptolabchatservice.handler;

public enum Route {


    LOGIN("/login"),
    JOIN("/join"),
    LEAVE("/leave"),
    DISCONNECT("/disconnect"),
    LIST("/list"),
    USER("/read_message");


    private final String stringValue;


    Route(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }


}
