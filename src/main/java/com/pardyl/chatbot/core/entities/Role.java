package com.pardyl.chatbot.core.entities;

public interface Role {
    /**
     * Returns role's display name
     */
    String getName();

    /**
     * Returns role's implementation-dependent ID
     */
    String getId();
}
