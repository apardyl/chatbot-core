package com.pardyl.chatbot.core;

import com.pardyl.chatbot.core.entities.Channel;
import com.pardyl.chatbot.core.events.EventProcessor;

import java.util.List;
import java.util.function.Function;

public abstract class BotConfiguration {
    public abstract List<EventProcessor> getEventProcessors();

    public long getTypingSpeed() {
        return 8;
    }

    public double getTypingSpeedMaxDeviation() {
        return 0.2;
    }

    public Function<BotInstance, Channel> getLogChannel() {
        return null;
    }
}
