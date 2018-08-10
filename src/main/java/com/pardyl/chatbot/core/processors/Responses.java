package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.BotInstance;
import com.pardyl.chatbot.core.entities.Message;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class Responses {
    public interface ResponseCollectionBuilder {
        Iterable<Message> createMessages(Message originalMessage, BotInstance botInstance);
    }

    public interface ResponseBuilder {
        Message createMessages(Message originalMessage, BotInstance botInstance);
    }

    public static MessageResponse customMessages(ResponseCollectionBuilder responseBuilder) {
        return message -> bot -> message.getChannel().sendMessage(responseBuilder.createMessages(message, bot), bot);
    }

    public static MessageResponse customMessage(ResponseBuilder responseBuilder) {
        return customMessages(((originalMessage, factory) -> Collections.singleton(responseBuilder.createMessages(originalMessage, factory))));
    }

    public static MessageResponse customMessagesTyping(ResponseCollectionBuilder responseBuilder) {
        return message -> bot -> message.getChannel().sendMessageTyping(responseBuilder.createMessages(message, bot), bot);
    }

    public static MessageResponse customMessageTyping(ResponseBuilder responseBuilder) {
        return customMessagesTyping(((originalMessage, factory) -> Collections.singleton(responseBuilder.createMessages(originalMessage, factory))));
    }

    public static MessageResponse customPrivateMessages(ResponseCollectionBuilder responseBuilder) {
        return message -> bot -> message.getAuthor().getPrivateChannel().sendMessage(responseBuilder.createMessages(message, bot), bot);
    }

    public static MessageResponse customPrivateMessagesTyping(ResponseCollectionBuilder responseBuilder) {
        return message -> bot -> message.getAuthor().getPrivateChannel().sendMessageTyping(responseBuilder.createMessages(message, bot), bot);
    }

    public static MessageResponse text(String text) {
        return customMessage(((originalMessage, bot) -> bot.getMessageFactory().appendText(text).build()));
    }

    public static MessageResponse textTyping(String text) {
        return customMessageTyping(((originalMessage, bot) -> bot.getMessageFactory().appendText(text).build()));
    }

    public static MessageResponse textTyping(Collection<String> texts) {
        return customMessagesTyping(((originalMessage, bot) -> texts.stream().map(t -> bot.getMessageFactory().appendText(t).build()).collect(Collectors.toList())));
    }

    public static MessageResponse respondText(String text) {
        return customMessage(((originalMessage, bot) -> bot.getMessageFactory().appendMentionUser(originalMessage.getAuthor()).appendText(" " + text).build()));
    }

    public static MessageResponse respondTextTyping(String text) {
        return customMessageTyping(((originalMessage, bot) -> bot.getMessageFactory().appendMentionUser(originalMessage.getAuthor()).appendText(" " + text).build()));
    }

    public static MessageResponse privateText(String text) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendMessage(text, bot));
    }

    public static MessageResponse privateTextTyping(String text) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendMessageTyping(text, bot));
    }

    public static MessageResponse sendFile(InputStream data, String uploadName) {
        return (message -> bot -> message.getChannel().sendFile(data, uploadName, null, bot));
    }

    public static MessageResponse sendPrivateFile(InputStream data, String uploadName) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendFile(data, uploadName, null, bot));
    }
}
