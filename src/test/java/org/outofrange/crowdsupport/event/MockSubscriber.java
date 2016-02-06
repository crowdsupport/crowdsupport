package org.outofrange.crowdsupport.event;

import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MockSubscriber<T> {
    private final List<T> events = new ArrayList<>();

    public MockSubscriber() {
        EventDispatcher.register(this);
    }

    @Subscribe
    public void receive(T event) {
        events.add(event);
    }

    public List<T> receivedEvents() {
        return events;
    }

    public int count() {
        return events.size();
    }

    public void unsubscribe() {
        EventDispatcher.unregister(this);
    }
}
