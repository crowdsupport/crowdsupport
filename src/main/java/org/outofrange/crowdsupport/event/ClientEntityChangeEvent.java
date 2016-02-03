package org.outofrange.crowdsupport.event;

public class ClientEntityChangeEvent<T> extends EntityChangeEvent<T> implements ClientEvent {
    private final String topic;

    public ClientEntityChangeEvent(ChangeType changeType, T payload, String topic) {
        super(changeType, payload);
        this.topic = topic;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
