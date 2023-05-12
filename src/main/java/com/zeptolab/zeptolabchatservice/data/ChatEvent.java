package com.zeptolab.zeptolabchatservice.data;

import com.zeptolab.zeptolabchatservice.repositories.type.MessageType;

public record ChatEvent(MessageType messageType, String text, String channel, String name) {

}
