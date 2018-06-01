package com.pardyl.chatbot.core.entities;

public interface Reaction {
    /**
     * Returns reaction's display name
     */
    String getName();

    /**
     * Returns reaction's implementation-dependent ID
     */
    String getId();
}
