package com.pardyl.chatbot.core;

import com.pardyl.chatbot.core.entities.Channel;
import com.pardyl.chatbot.core.events.Event;
import com.pardyl.chatbot.core.events.EventAction;
import com.pardyl.chatbot.core.events.EventProcessor;
import com.pardyl.chatbot.core.entities.MessageFactory;
import com.pardyl.chatbot.core.entities.Server;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class BotInstance {
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private final List<EventProcessor> eventProcessors;
    private final long typingSpeed;
    private final double typingSpeedMaxDeviation;
    private final Function<BotInstance, Channel> getLogChannel;

    public final boolean addEventProcessor(EventProcessor eventProcessor) {
        return eventProcessors.add(eventProcessor);
    }

    public final boolean removeEventProcessor(EventProcessor eventProcessor) {
        return eventProcessors.remove(eventProcessor);
    }

    protected BotInstance(BotConfiguration configuration) {
        this.eventProcessors = new ArrayList<>();
        this.eventProcessors.addAll(configuration.getEventProcessors());
        this.typingSpeed = configuration.getTypingSpeed();
        this.typingSpeedMaxDeviation = configuration.getTypingSpeedMaxDeviation();
        this.getLogChannel = configuration.getLogChannel();
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

    protected void logException(Exception ex) {
        Channel logChannel = getLogChannel.apply(this);
        if (logChannel != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logChannel.sendMessage(sw.toString().substring(0, 1000) + "...", this);
        }
        System.err.println(ex.toString());
        ex.printStackTrace();
    }

    public final void process(Event event) {
        eventProcessors.parallelStream().forEach(processor -> {
            try {
                EventAction action = processor.trigger(event);
                if (action != null) {
                    pool.execute(() -> {
                        try {
                            action.run(this);
                        } catch (Exception ex) {
                            logException(ex);
                        }
                    });
                }
            } catch (Exception ex) {
                logException(ex);
            }
        });
    }

    public final void schedule(EventAction action, long delayMilliseconds) {
        pool.schedule(() -> {
            try {
                action.run(this);
            } catch (Exception ex) {
                logException(ex);
            }
        }, delayMilliseconds, TimeUnit.MILLISECONDS);
    }

    public final long getTypingSpeed() {
        return typingSpeed;
    }

    public final double getTypingSpeedMaxDeviation() {
        return typingSpeedMaxDeviation;
    }
}
