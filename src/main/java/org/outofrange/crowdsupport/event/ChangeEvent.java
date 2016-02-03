package org.outofrange.crowdsupport.event;

public interface ChangeEvent extends PayloadEvent<Object> {
    ChangeType getChangeType();
}
