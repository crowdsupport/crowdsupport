package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.BaseDto;

public interface ChangeEvent extends Event {
    enum Type {
        ADD, REFRESH, REMOVE
    }

    default String getEntity() {
        return getPayload().getClass().getSimpleName();
    }

    Type getChangeType();

    Object getPayload();

    default Long getId() {
        final Object payload = getPayload();

        if (getPayload() instanceof BaseDto) {
            return ((BaseDto) payload).getId();
        } else {
            return null;
        }
    }
}
