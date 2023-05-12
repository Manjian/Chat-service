package com.zeptolab.zeptolabchatservice.handler;

public enum NameSpace {


    LOGIN("/login"),
    JOIN("/join"),
    LEAVE("/leave"),
    DISCONNECT("/disconnect"),
    LIST("/list"),
    USER("/users"),
    CHAT("/send_message");


    private final String stringValue;


    NameSpace(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }


}
