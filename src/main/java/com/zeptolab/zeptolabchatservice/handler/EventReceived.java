package com.zeptolab.zeptolabchatservice.handler;

import com.corundumstudio.socketio.listener.DataListener;
import com.zeptolab.zeptolabchatservice.data.*;

public interface EventReceived  {

    DataListener<LoginEvent> onLoginEvent();

    DataListener<JoinEvent> onChannelJoinEven();

    DataListener<EmptyEvent> onChannelLeaveEvent();

    DataListener<EmptyEvent> onDisconnectEvent();

    DataListener<EmptyEvent> onGetChannelsListEvent();

    DataListener<UserChannelEvent> onGetUserListEvent();


}
