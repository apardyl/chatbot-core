package com.pardyl.chatbot.core.entities;

public interface MessageReaction {
    Emote getEmote();

    int getCount();

    Message getMessage();
}
