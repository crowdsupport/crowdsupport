package org.outofrange.crowdsupport.event;

public class EntityChangeEvent<T> implements ChangeEvent {
    private Type changeType;
    private T payload;

    public EntityChangeEvent(Type changeType, T payload) {
        this.changeType = changeType;
        this.payload = payload;
    }

    @Override
    public Type getChangeType() {
        return changeType;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
