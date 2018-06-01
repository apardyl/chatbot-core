package com.pardyl.chatbot.core;

import com.pardyl.chatbot.core.com.pardyl.chatbot.events.Event;
import com.pardyl.chatbot.core.com.pardyl.chatbot.events.EventAction;
import com.pardyl.chatbot.core.com.pardyl.chatbot.events.EventProcessor;
import com.pardyl.chatbot.core.entities.MessageFactory;
import com.pardyl.chatbot.core.entities.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BotInstance {
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    protected List<EventProcessor> eventProcessors;

    protected BotInstance(BotConfiguration configuration) {
        this.eventProcessors = new ArrayList<>();
        this.eventProcessors.addAll(configuration.getEventProcessors());
    }

    public abstract void run();

    public abstract List<Server> getServers();

    /**
     * Returns channel matching given name. If more than one server matches returns any.
     */
    public Server getServerForName(String name) {
        return getServers().stream().filter(server -> server.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Returns channel matching given id.
     */
    public Server getServerForId(String id) {
        return getServers().stream().filter(server -> server.getId().equals(id)).findAny().orElse(null);
    }

    public abstract MessageFactory getMessageFactory();

    public void process(Event event) {
        eventProcessors.parallelStream().forEach(processor -> {
            EventAction action = processor.trigger(event);
            if (action != null) {
                pool.execute(() -> action.run(this));
            }
        });
    }

    public void schedule(EventAction action, long delaySeconds) {
        pool.schedule(() -> action.run(this), delaySeconds, TimeUnit.SECONDS);
    }
}
