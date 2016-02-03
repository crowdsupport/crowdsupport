package org.outofrange.crowdsupport.event;

public class EmptyClientEvent implements ClientEvent {
    private final String topic;

    public EmptyClientEvent(String topic) {
        this.topic = topic;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
