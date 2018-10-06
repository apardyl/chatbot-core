package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.entities.MessageReaction;
import com.pardyl.chatbot.core.entities.User;

public final class OnReactionAddedEvent implements Event {
    public final MessageReaction reaction;

    public final User reactingUser;

    public OnReactionAddedEvent(MessageReaction reaction, User reactingUser) {
        this.reaction = reaction;
        this.reactingUser = reactingUser;
    }
}
