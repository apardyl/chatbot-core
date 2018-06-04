package com.pardyl.chatbot.core.entities;

import com.pardyl.chatbot.core.BotInstance;

import java.util.List;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public abstract class Channel {
    private static final int TYPING_CHARS_PER_SECOND = 8;
    private static final double TYPING_SPEED_MAX_DEVIATION = 0.25;

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

    public void sendMessage(String text, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendText(text).build(), bot);
    }

    public void sendMessage(Reaction reaction, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendReaction(reaction).build(), bot);
    }

    public void sendMessage(User mention, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendMentionUser(mention).build(), bot);
    }

    public void sendMessage(Role mention, BotInstance bot) {
        sendMessage(bot.getMessageFactory().appendMentionRole(mention).build(), bot);
    }

    public abstract void sendFile(File file, String uploadName, Message message);

    public abstract void addReaction(Message message, Reaction reaction, BotInstance bot);

    public abstract void sendIsTyping(BotInstance bot);

    public void sendMessageTyping(Message message, BotInstance bot) {
        sendIsTyping(bot);
        long delay = (long) (TimeUnit.SECONDS.toMillis(1) * message.getText().length()
                / TYPING_CHARS_PER_SECOND * ThreadLocalRandom.current()
                .nextDouble(1 - TYPING_SPEED_MAX_DEVIATION, 1 + TYPING_SPEED_MAX_DEVIATION));
        bot.schedule((instance) -> sendMessage(message, instance), delay);
    }

    public void sendMessageTyping(String text, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendText(text).build(), bot);
    }

    public void sendMessageTyping(Reaction reaction, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendReaction(reaction).build(), bot);
    }

    public void sendMessageTyping(User mention, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendMentionUser(mention).build(), bot);
    }

    public void sendMessageTyping(Role mention, BotInstance bot) {
        sendMessageTyping(bot.getMessageFactory().appendMentionRole(mention).build(), bot);
    }
}
