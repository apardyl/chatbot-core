package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class Filters {
    public static MessageFilter sendBy(User user) {
        return message -> message.getAuthor().equals(user);
    }

    public static MessageFilter sendById(String user) {
        return message -> message.getAuthor().getId().equals(user);
    }

    public static MessageFilter sendByName(String user) {
        return message -> message.getAuthor().getName().equals(user);
    }

    public static MessageFilter inChannel(Channel channel) {
        return message -> message.getChannel().equals(channel);
    }

    public static MessageFilter inChannelId(String channel) {
        return message -> message.getChannel().getId().equals(channel);
    }

    public static MessageFilter inChannelName(String channel) {
        return message -> message.getChannel().getName().equals(channel);
    }

    public static MessageFilter inServer(Server server) {
        return message -> message.getChannel().getServer().equals(server);
    }

    public static MessageFilter inServerId(String server) {
        return message -> message.getChannel().getServer().getId().equals(server);
    }

    public static MessageFilter inServerName(String server) {
        return message -> message.getChannel().getServer().getName().equals(server);
    }

    public static MessageFilter senderHasRole(Role role) {
        return message -> message.getChannel().getServer().hasRole(message.getAuthor(), role);
    }

    public static MessageFilter senderHasRoleId(String role) {
        return message -> message.getChannel().getServer().hasRole(message.getAuthor(), message.getChannel().getServer().getRoleForId(role));
    }

    public static MessageFilter senderHasRoleName(String role) {
        return message -> message.getChannel().getServer().hasRole(message.getAuthor(), message.getChannel().getServer().getRoleForName(role));
    }

    public static MessageFilter mentionsUser(User user) {
        return message -> message.getMentionedUsers().contains(user);
    }

    public static MessageFilter mentionsUserId(String user) {
        return message -> message.getMentionedUsers().contains(message.getChannel().getServer().getUserForId(user));
    }

    public static MessageFilter mentionsUserName(String user) {
        return message -> message.getMentionedUsers().contains(message.getChannel().getServer().getUserForName(user));
    }

    public static MessageFilter mentionsRole(Role role) {
        return message -> message.getMentionedRoles().contains(role);
    }

    public static MessageFilter mentionsRoleId(String role) {
        return message -> message.getMentionedRoles().contains(message.getChannel().getServer().getRoleForId(role));
    }

    public static MessageFilter mentionsRoleName(String role) {
        return message -> message.getMentionedRoles().contains(message.getChannel().getServer().getRoleForName(role));
    }

    public static MessageFilter mentionsChannel(Channel channel) {
        return message -> message.getMentionedChannels().contains(channel);
    }

    public static MessageFilter mentionsChannelId(String channel) {
        return message -> message.getMentionedChannels().contains(message.getChannel().getServer().getChannelForId(channel));
    }

    public static MessageFilter mentionsChannelName(String channel) {
        return message -> message.getMentionedChannels().contains(message.getChannel().getServer().getChannelForName(channel));
    }

    public static MessageFilter textIs(String text) {
        return message -> message.getText().equals(text);
    }

    public static MessageFilter textContains(String text) {
        return message -> message.getText().contains(text);
    }

    public static MessageFilter textStartsWith(String text) {
        return message -> message.getText().startsWith(text);
    }

    public static MessageFilter textEndsWith(String text) {
        return message -> message.getText().endsWith(text);
    }

    public static MessageFilter textMatches(String regex) {
        return message -> message.getText().matches(regex);
    }

    public static MessageFilter textToLowerIs(String text) {
        return message -> message.getText().toLowerCase().equals(text);
    }

    public static MessageFilter textToLowerContains(String text) {
        return message -> message.getText().toLowerCase().contains(text);
    }

    public static MessageFilter textToLowerStartsWith(String text) {
        return message -> message.getText().toLowerCase().startsWith(text);
    }

    public static MessageFilter textToLowerEndsWith(String text) {
        return message -> message.getText().toLowerCase().endsWith(text);
    }

    public static MessageFilter textToLowerMatcher(String regex) {
        return message -> message.getText().toLowerCase().matches(regex);
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        Predicate<T> predicate = (t) -> false;
        for (Predicate<T> p : predicates) {
            predicate = predicate.or(p);
        }
        return predicate;
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T>... predicates) {
        Predicate<T> predicate = (t) -> true;
        for (Predicate<? super T> p : predicates) {
            predicate = predicate.and(p);
        }
        return predicate;
    }

    /**
     * @param chance between 0 and 1
     */
    public static <T> Predicate<T> randomChance(double chance) {
        return (t) -> ThreadLocalRandom.current().nextDouble() < chance;
    }
}
