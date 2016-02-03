package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.model.BaseEntity;

public interface PayloadEvent<T> extends Event {
    @JsonProperty
    T getPayload();

    @JsonProperty
    default String getPayloadType() {
        final T payload = getPayload();

        return payload == null ? null : getPayload().getClass().getSimpleName();
    }
}
