package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public void sendMessage(Iterable<Message> messages, BotInstance bot) {
        messages.forEach(message -> sendMessage(message, bot));
    }

    public void sendMessage(String text, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendText(text).build(), bot);
    }

    public void sendMessage(Emote emote, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendEmote(emote).build(), bot);
    }

    public void sendMessage(User mention, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendMentionUser(mention).build(), bot);
    }

    public void sendMessage(Role mention, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendMentionRole(mention).build(), bot);
    }

    public abstract void sendFile(File file, String uploadName, Message message, BotInstance bot);

    public abstract void sendFile(InputStream data, String uploadName, Message message, BotInstance bot);

    public abstract void sendIsTyping(BotInstance bot);

    public void sendMessageTyping(Message message, BotInstance bot) {
        sendMessageTyping(Collections.singleton(message), bot);
    }

    public void sendMessageTyping(Iterable<Message> messages, BotInstance bot) {
        sendMessageTyping(messages.iterator(), bot);
    }

    public void sendMessageTyping(Iterator<Message> messages, BotInstance bot) {
        if (!messages.hasNext()) {
            return;
        }
        sendIsTyping(bot);
        Message m = messages.next();
        long delay = (long) (TimeUnit.SECONDS.toMillis(1) * m.getText().length()
                / bot.getTypingSpeed() * ThreadLocalRandom.current()
                .nextDouble(1 - bot.getTypingSpeedMaxDeviation(), 1 + bot.getTypingSpeedMaxDeviation()));
        bot.schedule((instance) -> {
            sendMessage(m, instance);
            sendMessageTyping(messages, bot);
        }, delay);
    }

    public void sendMessageTyping(String text, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendText(text).build(), bot);
    }

    public void sendMessageTyping(Collection<String> text, BotInstance bot) {
        sendMessageTyping(text.stream().map(t -> bot.getMessageFactory().appendText(t).build()).collect(Collectors.toList()), bot);
    }

    public void sendMessageTyping(Emote emote, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendEmote(emote).build(), bot);
    }

    public void sendMessageTyping(User mention, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendMentionUser(mention).build(), bot);
    }

    public void sendMessageTyping(Role mention, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendMentionRole(mention).build(), bot);
    }
}
