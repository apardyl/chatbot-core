package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class Filters {
    public static MessageFilter sendBy(User user) {
        return message -> message.getAuthor().equals(user);
    }

    public static MessageFilter inChannel(Channel channel) {
        return message -> message.getChannel().equals(channel);
    }

    public static MessageFilter inServer(Server server) {
        return message -> message.getChannel().getServer().equals(server);
    }

    public static MessageFilter senderHasRole(Role role) {
        return message -> message.getChannel().getServer().hasRole(message.getAuthor(), role);
    }

    public static MessageFilter mentions(User user) {
        return message -> message.getMentionedUsers().contains(user);
    }

    public static MessageFilter mentions(Role role) {
        return message -> message.getMentionedRoles().contains(role);
    }

    public static MessageFilter mentions(Channel channel) {
        return message -> message.getMentionedChannels().contains(channel);
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
