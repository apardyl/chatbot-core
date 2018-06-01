package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.entities.Message;

public final class OnMessageEvent implements Event {
    public final Message message;

    public OnMessageEvent(Message message) {
        this.message = message;
    }
}
