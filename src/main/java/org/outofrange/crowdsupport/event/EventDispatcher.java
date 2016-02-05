package org.outofrange.crowdsupport.event;

import com.google.common.eventbus.EventBus;

import java.util.List;

public class EventDispatcher {
    private static final EventBus INSTANCE = new EventBus();

    private EventDispatcher() {
        // no instantiation
    }

    /**
     * @see EventBus#register
     */
    public static void register(Object object) {
        INSTANCE.register(object);
    }

    /**
     * @see EventBus#post
     */
    public static void post(Object object) {
        INSTANCE.post(object);
    }
}