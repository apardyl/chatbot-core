package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class Filters {
    public static Predicate<Message> sendBy(User user) {
        return message -> message.getAuthor().equals(user);
    }

    public static Predicate<Message> inChannel(Channel channel) {
        return message -> message.getChannel().equals(channel);
    }

    public static Predicate<Message> inServer(Server server) {
        return message -> message.getChannel().getServer().equals(server);
    }

    public static Predicate<Message> senderHasRole(Role role) {
        return message -> message.getChannel().getServer().hasRole(message.getAuthor(), role);
    }

    public static Predicate<Message> mentions(User user) {
        return message -> message.getMentionedUsers().contains(user);
    }

    public static Predicate<Message> mentions(Role role) {
        return message -> message.getMentionedRoles().contains(role);
    }

    public static Predicate<Message> mentions(Channel channel) {
        return message -> message.getMentionedChannels().contains(channel);
    }

    public static Predicate<Message> textIs(String text) {
        return message -> message.getText().equals(text);
    }

    public static Predicate<Message> textContains(String text) {
        return message -> message.getText().contains(text);
    }

    public static Predicate<Message> textStartsWith(String text) {
        return message -> message.getText().startsWith(text);
    }

    public static Predicate<Message> textEndsWith(String text) {
        return message -> message.getText().endsWith(text);
    }

    public static Predicate<Message> textMatches(String regex) {
        return message -> message.getText().matches(regex);
    }

    public static Predicate<Message> textToLowerIs(String text) {
        return message -> message.getText().toLowerCase().equals(text);
    }

    public static Predicate<Message> textToLowerContains(String text) {
        return message -> message.getText().toLowerCase().contains(text);
    }

    public static Predicate<Message> textToLowerStartsWith(String text) {
        return message -> message.getText().toLowerCase().startsWith(text);
    }

    public static Predicate<Message> textToLowerEndsWith(String text) {
        return message -> message.getText().toLowerCase().endsWith(text);
    }

    public static Predicate<Message> textToLowerMatcher(String regex) {
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
