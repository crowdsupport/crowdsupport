package org.outofrange.crowdsupport.event;

import java.util.Arrays;
import java.util.List;

public class EmptyClientEvent implements ClientEvent {
    private final List<String> topics;

    public EmptyClientEvent(String... topics) {
        this.topics = Arrays.asList(topics);
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }
}
