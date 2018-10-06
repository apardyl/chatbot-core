package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.BotInstance;
import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.entities.Emote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
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

    public static MessageResponse randomText(List<String> texts) {
        return message -> text(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse randomTextTyping(List<String> texts) {
        return message -> textTyping(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse respondText(String text) {
        return customMessage(((originalMessage, bot) -> bot.getMessageFactory().appendMentionUser(originalMessage.getAuthor()).appendText(" " + text).build()));
    }

    public static MessageResponse respondTextTyping(String text) {
        return customMessageTyping(((originalMessage, bot) -> bot.getMessageFactory().appendMentionUser(originalMessage.getAuthor()).appendText(" " + text).build()));
    }

    public static MessageResponse randomRespondText(List<String> texts) {
        return message -> respondText(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse randomRespondTextTyping(List<String> texts) {
        return message -> respondTextTyping(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse privateText(String text) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendMessage(text, bot));
    }

    public static MessageResponse privateTextTyping(String text) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendMessageTyping(text, bot));
    }

    public static MessageResponse randomPrivateText(List<String> texts) {
        return message ->  privateText(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse randomPrivateTextTyping(List<String> texts) {
        return message ->  privateTextTyping(texts.get(ThreadLocalRandom.current().nextInt(texts.size()))).respondTo(message);
    }

    public static MessageResponse sendFile(InputStream data, String uploadName) {
        return (message -> bot -> message.getChannel().sendFile(data, uploadName, null, bot));
    }

    public static MessageResponse sendFile(String resourceName, Class resourceFor) {
        return message -> bot -> {
            try {
                InputStream st = resourceFor.getResourceAsStream(resourceName);
                message.getChannel().sendFile(st, resourceName, null, bot);
                st.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static MessageResponse sendRandomFile(List<String> resourceNames, Class resourceFor) {
        return message ->  sendFile(resourceNames.get(ThreadLocalRandom.current().nextInt(resourceNames.size())), resourceFor).respondTo(message);
    }

    public static MessageResponse sendPrivateFile(InputStream data, String uploadName) {
        return (message -> bot -> message.getAuthor().getPrivateChannel().sendFile(data, uploadName, null, bot));
    }

    public static MessageResponse emote(Emote emote) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendEmote(emote).build());
    }

    public static MessageResponse randomEmote(List<Emote> emotes) {
        return message -> emote(emotes.get(ThreadLocalRandom.current().nextInt(emotes.size()))).respondTo(message);
    }

    public static MessageResponse emoteId(String emote) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendEmote(
                originalMessage.getChannel().getServer().getEmoteForId(emote)).build());
    }

    public static MessageResponse randomEmoteId(List<String> emotes) {
        return message -> emoteId(emotes.get(ThreadLocalRandom.current().nextInt(emotes.size()))).respondTo(message);
    }

    public static MessageResponse emoteName(String emote) {
        return customMessage((originalMessage, botInstance) -> botInstance.getMessageFactory().appendEmote(
                originalMessage.getChannel().getServer().getEmoteForName(emote)).build());
    }

    public static MessageResponse randomEmoteName(List<String> emotes) {
        return message -> emoteName(emotes.get(ThreadLocalRandom.current().nextInt(emotes.size()))).respondTo(message);
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

    public static class RandomMessageResponse {
        final MessageResponse response;
        final double weight;

        private RandomMessageResponse(MessageResponse response, double weight) {
            this.response = response;
            this.weight = weight;
        }
    }

    public static MessageResponse randomResponse(MessageResponse... responses) {
        return randomResponse(Arrays.asList(responses));
    }

    public static MessageResponse randomResponse(List<MessageResponse> responses) {
        return message -> responses.get(ThreadLocalRandom.current().nextInt(responses.size())).respondTo(message);
    }

    public static MessageResponse randomResponseWeighted(RandomMessageResponse... responses) {
        return randomResponseWeighted(Arrays.asList(responses));
    }

    public static MessageResponse randomResponseWeighted(List<RandomMessageResponse> responses) {
        double sum = responses.stream().mapToDouble(resp -> resp.weight).sum();
        return message -> {
            double choice = ThreadLocalRandom.current().nextDouble(sum);
            double current = 0.0;
            for (RandomMessageResponse response : responses) {
                current += response.weight;
                if (current >= choice) {
                    return response.response.respondTo(message);
                }
            }
            return responses.get(responses.size() - 1).response.respondTo(message);
        };
    }

    public static RandomMessageResponse chance(MessageResponse response, double weight) {
        return new RandomMessageResponse(response, weight);
    }
}
