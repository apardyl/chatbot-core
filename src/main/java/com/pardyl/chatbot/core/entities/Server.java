package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.util.List;

public abstract class Server {
    /**
     * Returns server's display name
     */
    public abstract String getName();

    /**
     * Returns server's implementation-dependent ID
     */
    public abstract String getId();

    public abstract List<Channel> getChannels();

    /**
     * Returns channel matching given name. If more than one channel matches returns any.
     */
    public Channel getChannelForName(String name) {
        return getChannels().stream().filter(channel -> channel.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns channel matching given id.
     */
    public Channel getChannelForId(String id) {
        return getChannels().stream().filter(channel -> channel.getId().equals(id)).findAny().orElse(null);
    }

    public abstract List<User> getUsers();

    /**
     * Returns user matching given name. If more than one channel matches returns any.
     */
    public User getUserForName(String name) {
        return getUsers().stream().filter(user -> user.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns user matching given id.
     */
    public User getUserForId(String id) {
        return getUsers().stream().filter(user -> user.getId().equals(id)).findAny().orElse(null);
    }

    public abstract List<Role> getRoles();

    /**
     * Returns user matching given name. If more than one user matches returns any.
     */
    public Role getRoleForName(String name) {
        return getRoles().stream().filter(role -> role.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns user matching given id.
     */
    public Role getRoleForId(String id) {
        return getRoles().stream().filter(role -> role.getId().equals(id)).findAny().orElse(null);
    }

    public abstract List<Reaction> getReactions();

    /**
     * Returns reaction matching given name. If more than one reaction matches returns any.
     */
    public Reaction getReactionForName(String name) {
        return getReactions().stream().filter(reaction -> reaction.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns reaction matching given id.
     */
    public Reaction getReactionForId(String id) {
        return getReactions().stream().filter(reaction -> reaction.getId().equals(id)).findAny().orElse(null);
    }

    public abstract void kickUser(User user, BotInstance bot);
}
