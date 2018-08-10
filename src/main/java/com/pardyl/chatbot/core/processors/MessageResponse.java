package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.Message;
import com.pardyl.chatbot.core.events.EventAction;

public interface MessageResponse {
    EventAction respondTo(Message message);
}
