package org.outofrange.crowdsupport.event;

import com.google.common.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventDispatcher {
    private static final EventBus INSTANCE = new EventBus();
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

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
        EXECUTOR.execute(() -> INSTANCE.post(object));
    }

    public static void post(List<Object> objects) {
        EXECUTOR.execute(() -> objects.forEach(INSTANCE::post));
    }
}
