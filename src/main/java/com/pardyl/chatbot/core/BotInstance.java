package com.pardyl.chatbot.core;

import com.pardyl.chatbot.core.events.Event;
import com.pardyl.chatbot.core.events.EventAction;
import com.pardyl.chatbot.core.events.EventProcessor;
import com.pardyl.chatbot.core.entities.MessageFactory;
import com.pardyl.chatbot.core.entities.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BotInstance {
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private final List<EventProcessor> eventProcessors;

    public final boolean addEventProcessor(EventProcessor eventProcessor) {
        return eventProcessors.add(eventProcessor);
    }

    public final boolean removeEventProcessor(EventProcessor eventProcessor) {
        return eventProcessors.remove(eventProcessor);
    }

    protected BotInstance(BotConfiguration configuration) {
        this.eventProcessors = new ArrayList<>();
        this.eventProcessors.addAll(configuration.getEventProcessors());
    }

    public abstract void run();

    public abstract void shutdown();

    public InputStream runAdminTask(String taskName) {
        throw new UnsupportedOperationException();
    }

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

    public final void process(Event event) {
        eventProcessors.parallelStream().forEach(processor -> {
            EventAction action = processor.trigger(event);
            if (action != null) {
                pool.execute(() -> action.run(this));
            }
        });
    }

    public final void schedule(EventAction action, long delayMilliseconds) {
        pool.schedule(() -> action.run(this), delayMilliseconds, TimeUnit.MILLISECONDS);
    }
}
