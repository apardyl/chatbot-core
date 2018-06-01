package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.util.List;
import java.util.Optional;

public abstract class Channel {
    /**
     * Returns channel's display name
     */
    public abstract String getName();

    /**
     * Returns channel's implementation-dependent ID
     */
    public abstract String getId();

    public abstract Server getServer();

    public abstract List<User> getMembers();

    public User getMemeberForName(String name) {
        return getMembers().stream().filter(user -> user.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns user matching given id.
     */
    public User getMemeberForId(String id) {
        return getMembers().stream().filter(user -> user.getId().equals(id)).findAny().orElse(null);
    }

    public abstract void sendMessage(Message message, BotInstance bot);

    public abstract void addReaction(Message message, Reaction reaction, BotInstance bot);
}
