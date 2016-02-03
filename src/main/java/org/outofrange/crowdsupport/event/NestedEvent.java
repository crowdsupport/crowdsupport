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
        Event.super.publish();
        subEvents.forEach(Event::publish);
    }
}
