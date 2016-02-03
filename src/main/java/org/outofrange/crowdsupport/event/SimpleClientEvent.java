package org.outofrange.crowdsupport.event;

import java.util.Arrays;
import java.util.List;

public class SimpleClientEvent implements ClientEvent {
    private final List<String> topics;
    private Object payload;

    public SimpleClientEvent(String... topics) {
        this.topics = Arrays.asList(topics);
    }

    public SimpleClientEvent(Object payload, String... topics) {
        this(topics);
        this.payload = payload;
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }
}
