package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.BotInstance;
import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.entities.Reaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static MessageResponse reaction(Reaction reaction) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendReaction(reaction).build());
    }

    public static MessageResponse reactionId(String reaction) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendReaction(
                originalMessage.getChannel().getServer().getReactionForId(reaction)).build());
    }

    public static MessageResponse reactionName(String reaction) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendReaction(
                originalMessage.getChannel().getServer().getReactionForName(reaction)).build());
    }

    public static MessageResponse adminTask(Function<Message, String> taskName) {
        return message -> bot -> {
            try {
                InputStream stream = bot.runAdminTask(taskName.apply(message));
                BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        message.getChannel().sendMessage(bot.getMessageFactory().appendText(line).build(), bot);
                    }
                }
            } catch (Exception ex) {
                message.getChannel().sendMessage(bot.getMessageFactory().appendText(ex.toString()).build(), bot);
            }
        };
    }

    public static MessageResponse adminTask(String taskName) {
        return adminTask(m -> taskName);
    }

    public static MessageResponse shutdown() {
        return message -> BotInstance::shutdown;
    }

    public static MessageResponse responseList(MessageResponse... responses) {
        return message -> bot -> Stream.of(responses).forEach(resp -> resp.respondTo(message).run(bot));
    }

    public static <T> T randomChoice(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    @SafeVarargs
    public static <T> T randomChoice(T... choices) {
        return choices[ThreadLocalRandom.current().nextInt(choices.length)];
    }

    public static class RandomMessageResponse {
        final MessageResponse response;
        final double weight;

        private RandomMessageResponse(MessageResponse response, double weight) {
            this.response = response;
            this.weight = weight;
        }
    }

    public static MessageResponse randomResponse(RandomMessageResponse... responses) {
        double sum = Stream.of(responses).mapToDouble(resp -> resp.weight).sum();
        double choice = ThreadLocalRandom.current().nextDouble(sum);
        double current = 0.0;
        for (RandomMessageResponse response : responses) {
            current += response.weight;
            if (current >= choice) {
                return response.response;
            }
        }
        return responses[responses.length - 1].response;
    }

    public static RandomMessageResponse chance(MessageResponse response, double weight) {
        return new RandomMessageResponse(response, weight);
    }
}
