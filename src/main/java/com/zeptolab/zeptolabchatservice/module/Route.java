package com.zeptolab.zeptolabchatservice.module;

public enum Route {


    LOGIN("login"),
    JOIN("join"),
    DISCONNECT("disconnect"),
    LIST("list"),
    USER("users");


    private String stringValue;


    Route(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }


}
