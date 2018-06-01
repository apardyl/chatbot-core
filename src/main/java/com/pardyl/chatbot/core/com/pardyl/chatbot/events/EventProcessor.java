package com.pardyl.chatbot.core.com.pardyl.chatbot.events;

public interface EventProcessor {
    /**
     * Returns event handler to be queued or null.
     */
    EventAction trigger(Event event);
}
