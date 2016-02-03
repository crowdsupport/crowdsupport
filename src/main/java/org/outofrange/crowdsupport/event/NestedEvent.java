package org.outofrange.crowdsupport.event;

import java.util.ArrayList;
import java.util.List;

public class NestedEvent implements Event {
    private final List<Event> subEvents = new ArrayList<>();

    public NestedEvent addEvent(Event event) {
        subEvents.add(event);
        return this;
    }

    @Override
    public void publish() {
        List<Event> events = new ArrayList<>(subEvents.size() + 1);
        events.add(this);
        events.addAll(subEvents);
        EventDispatcher.post(events);
    }
}
