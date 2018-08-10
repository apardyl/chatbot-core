package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.events.EventAction;
import com.pardyl.chatbot.core.events.EventProcessor;

public final class Processors {
    public static EventProcessor firstMatchProcessor(EventProcessor... processors) {
        return event -> {
            for (EventProcessor processor : processors) {
                EventAction action = processor.trigger(event);
                if (action != null) {
                    return action;
                }
            }
            return null;
        };
    }
}
