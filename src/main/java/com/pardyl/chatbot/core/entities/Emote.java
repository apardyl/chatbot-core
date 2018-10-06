package com.pardyl.chatbot.core.entities;

public interface Emote {
    /**
     * Returns emote's display name
     */
    String getName();

    /**
     * Returns emote's implementation-dependent ID
     */
    String getId();
}
