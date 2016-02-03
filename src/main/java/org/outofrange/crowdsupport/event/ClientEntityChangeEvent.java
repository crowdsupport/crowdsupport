package org.outofrange.crowdsupport.event;

import java.util.Arrays;
import java.util.List;

public class ClientEntityChangeEvent<T> extends EntityChangeEvent<T> implements ClientEvent {
    private final List<String> topics;

    public ClientEntityChangeEvent(ChangeType changeType, T payload, String... topics) {
        this(changeType, payload, Arrays.asList(topics));
    }

    public ClientEntityChangeEvent(ChangeType changeType, T payload, List<String> topics) {
        super(changeType, payload);
        this.topics = topics;
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }
}
