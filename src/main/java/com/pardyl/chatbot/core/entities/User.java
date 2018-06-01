package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

public interface User {
    /**
     * Returns user's display name
     */
    String getName();

    /**
     * Returns user's implementation-dependent ID
     */
    String getId();

    Channel getPrivateChannel();
}
