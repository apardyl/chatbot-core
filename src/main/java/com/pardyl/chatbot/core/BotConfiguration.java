package com.pardyl.chatbot.core;

import com.pardyl.chatbot.core.events.EventProcessor;

import java.util.List;

public abstract class BotConfiguration {
    public abstract List<EventProcessor> getEventProcessors();

    public long getTypingSpeed() {
        return 8;
    }

    public double getTypingSpeedMaxDeviation() {
        return 0.25;
    }
}
