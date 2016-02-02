package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Event {
    @JsonProperty
    default String name() {
        return getClass().getSimpleName();
    }
}
