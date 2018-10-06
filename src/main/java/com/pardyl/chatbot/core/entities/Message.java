package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.util.List;

public interface Message {
    Channel getChannel();

    User getAuthor();

    String getText();

    List<User> getMentionedUsers();

    List<Role> getMentionedRoles();

    List<Channel> getMentionedChannels();

    void pin(BotInstance bot);

    void unpin(BotInstance bot);

    void delete(BotInstance bot);

    void addReaction(Emote emote, BotInstance bot);
}
