package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.entities.Reaction;

public final class OnReactionAddedEvent implements Event {
    public final Message message;
    public final Reaction reaction;
    public final int count;

    public OnReactionAddedEvent(Message message, Reaction reaction, int count) {
        this.message = message;
        this.reaction = reaction;
        this.count = count;
    }
}
