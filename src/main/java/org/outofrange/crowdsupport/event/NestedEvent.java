package org.outofrange.crowdsupport.event;

import java.util.ArrayList;
import java.util.List;

public class NestedEvent implements Event {
    private final List<Event> subEvents = new ArrayList<>();

    protected void addSubEvent(Event event) {
        subEvents.add(event);
    }

    @Override
    public void publish() {
        Event.super.publish();
        subEvents.forEach(Event::publish);
    }
}
