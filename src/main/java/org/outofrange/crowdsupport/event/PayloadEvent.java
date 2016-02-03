package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.model.BaseEntity;

public interface PayloadEvent<T> extends Event {
    T getPayload();

    default String getPayloadType() {
        return getPayload().getClass().getSimpleName();
    }

    default Long getId() {
        final T payload = getPayload();

        if (payload instanceof BaseDto) {
            return ((BaseDto) payload).getId();
        } else if (getPayload() instanceof BaseEntity) {
            return ((BaseEntity) payload).getId();
        } else {
            return null;
        }
    }
}
