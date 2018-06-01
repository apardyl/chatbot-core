package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.entities.Reaction;

public final class OnReactionAddedEvent implements Event {
    public final Message message;
    public final Reaction reaction;

    public OnReactionAddedEvent(Message message, Reaction reaction) {
        this.message = message;
        this.reaction = reaction;
    }
}
