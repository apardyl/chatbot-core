package com.pardyl.chatbot.core.com.pardyl.chatbot.events;

import com.pardyl.chatbot.core.BotInstance;

public interface EventAction {
    void run(BotInstance instance);
}
