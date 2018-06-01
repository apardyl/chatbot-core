package com.pardyl.chatbot.core.entities;

public interface MessageFactory {
    Message build();

    MessageFactory appendText(String text);

    MessageFactory appendMentionUser(User user);

    MessageFactory appendMentionRole(Role role);

    MessageFactory appendReaction(Reaction reaction);
}
