package org.outofrange.crowdsupport.event;

public class EntityChangeEvent<T> implements ChangeEvent {
    private ChangeType changeType;
    private T payload;

    public EntityChangeEvent(ChangeType changeType, T payload) {
        this.changeType = changeType;
        this.payload = payload;
    }

    @Override
    public ChangeType getChangeType() {
        return changeType;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
