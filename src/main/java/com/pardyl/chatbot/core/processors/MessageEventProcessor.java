package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.events.Event;
import com.pardyl.chatbot.core.events.EventAction;
import com.pardyl.chatbot.core.events.EventProcessor;
import com.pardyl.chatbot.core.events.OnMessageEvent;

import java.util.function.Predicate;

public abstract class MessageEventProcessor implements EventProcessor {
    public static class MessageEventProcessorBuilder {
        private Predicate<Message> predicate = (message -> true);

        private MessageEventProcessorBuilder(Predicate<Message>[] predicates) {
            for (Predicate<Message> p : predicates) {
                predicate = predicate.and(p);
            }
        }

        public MessageEventProcessor respond(EventAction action) {
            return respond(m -> action);
        }

        public MessageEventProcessor respond(MessageResponse response) {
            return new MessageEventProcessor() {
                @Override
                public EventAction trigger(Event event) {
                    return event instanceof OnMessageEvent && predicate.test(((OnMessageEvent) event).message) ?
                            response.respondTo(((OnMessageEvent) event).message) : null;
                }
            };
        }

        public MessageEventProcessor chain(EventProcessor processor) {
            return new MessageEventProcessor() {
                @Override
                public EventAction trigger(Event event) {
                    return event instanceof OnMessageEvent && predicate.test(((OnMessageEvent) event).message) ?
                            processor.trigger(event) : null;
                }
            };
        }
    }

    private MessageEventProcessor() {
    }

    /**
     * Trigger action if message matches all given predicates
     */
    @SafeVarargs
    public static MessageEventProcessorBuilder on(Predicate<Message>... predicates) {
        return new MessageEventProcessorBuilder(predicates);
    }
}
