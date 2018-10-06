package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.util.List;
import java.util.stream.Collectors;

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

    public boolean isMember(User user) {
        return getUsers().contains(user);
    }

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

    public abstract List<Emote> getEmotes();

    /**
     * Returns emote matching given name. If more than one emote matches returns any.
     */
    public Emote getEmoteForName(String name) {
        return getEmotes().stream().filter(emote -> emote.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns emote matching given id.
     */
    public Emote getEmoteForId(String id) {
        return getEmotes().stream().filter(emote -> emote.getId().equals(id)).findAny().orElse(null);
    }

    public abstract void kickUser(User user, BotInstance bot);

    public abstract void banUser(User user, BotInstance bot);

    public abstract boolean isUserOnline(User user);

    public List<User> getOnlineUsers() {
        return getUsers().stream().filter(this::isUserOnline).collect(Collectors.toList());
    }

    public abstract List<Role> getRolesForUser(User user);

    public abstract List<User> getUsersForRole(Role role);

    public boolean hasRole(User user, Role role) {
        return getRolesForUser(user).contains(role);
    }
}
