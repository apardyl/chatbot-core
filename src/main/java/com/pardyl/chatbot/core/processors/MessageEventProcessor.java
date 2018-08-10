package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.events.Event;
import com.pardyl.chatbot.core.events.EventAction;
import com.pardyl.chatbot.core.events.EventProcessor;
import com.pardyl.chatbot.core.events.OnMessageEvent;

import java.util.function.Predicate;

public final class MessageEventProcessor implements EventProcessor {
    private final Predicate<Message> predicate;
    private MessageResponse response;

    private static class MessageEventProcessorBuilder {
        private Predicate<Message> predicateM = (message -> true);

        private MessageEventProcessorBuilder(Predicate<Message>[] predicates) {
            for (Predicate<Message> p : predicates) {
                predicateM = predicateM.and(p);
            }
        }

        public MessageEventProcessor respond(EventAction action) {
            return new MessageEventProcessor(predicateM, (m) -> action);
        }

        public MessageEventProcessor respond(MessageResponse response) {
            return new MessageEventProcessor(predicateM, response);
        }
    }

    private MessageEventProcessor(Predicate<Message> predicate, MessageResponse response) {
        this.predicate = predicate;
        this.response = response;
    }

    /**
     * Trigger action if message matches all given predicates
     */
    @SafeVarargs
    public static MessageEventProcessorBuilder on(Predicate<Message>... predicates) {
        return new MessageEventProcessorBuilder(predicates);
    }

    @Override
    public final EventAction trigger(Event event) {
        return event instanceof OnMessageEvent && predicate.test(((OnMessageEvent) event).message) ?
                response.respondTo(((OnMessageEvent) event).message) : null;
    }
}
