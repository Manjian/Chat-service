package com.zeptolab.zeptolabchatservice.handler;

@FunctionalInterface
public interface OnLeaveCallback {

    void onChannelLeave(String channelName);
}
