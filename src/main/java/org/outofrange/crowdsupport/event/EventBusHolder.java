package org.outofrange.crowdsupport.event;

import com.google.common.eventbus.EventBus;

public class EventBusHolder {
    private static final EventBus INSTANCE = new EventBus();

    private EventBusHolder() {
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
