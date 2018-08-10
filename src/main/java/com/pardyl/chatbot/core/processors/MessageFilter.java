package com.pardyl.chatbot.core.processors;

import com.pardyl.chatbot.core.entities.Message;

import java.util.function.Predicate;

public interface MessageFilter extends Predicate<Message> {
}
