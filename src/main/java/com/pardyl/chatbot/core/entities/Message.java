package com.pardyl.chatbot.core.entities;

import java.util.List;

public interface Message {
    Channel getChannel();

    User getAuthor();

    String getText();

    List<User> getMentionedUsers();

    List<Role> getMentionedRoles();
}
