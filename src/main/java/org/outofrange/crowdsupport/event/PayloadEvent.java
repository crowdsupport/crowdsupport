package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.model.BaseEntity;

/**
 * A PayloadEvent is an Event with a generic payload
 *
 * @param <T> the type of the payload
 */
public interface PayloadEvent<T> extends Event {
    /**
     * Returns the payload.
     *
     * @return the payload
     */
    @JsonProperty
    T getPayload();

    /**
     * Returns the payload type. Default implementation will retrun payloads class name.
     *
     * @return the payload type
     */
    @JsonProperty
    default String getPayloadType() {
        final T payload = getPayload();

        return payload == null ? null : getPayload().getClass().getSimpleName();
    }
}
