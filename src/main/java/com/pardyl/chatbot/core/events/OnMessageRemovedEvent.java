package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.entities.Channel;

public final class OnMessageRemovedEvent implements Event {
    public final Channel channel;
    public final String messageId;

    public OnMessageRemovedEvent(Channel channel, String messageId) {
        this.channel = channel;
        this.messageId = messageId;
    }
}
