package com.pardyl.chatbot.core.events;

import com.pardyl.chatbot.core.BotInstance;

public interface EventAction {
    void run(BotInstance instance);
}
