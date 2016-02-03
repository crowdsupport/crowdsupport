package org.outofrange.crowdsupport.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;

public class SimpleClientEvent implements ClientEvent, PayloadEvent<Object> {
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

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("payload", payload)
                .append("topics", topics)
                .toString();
    }
}
