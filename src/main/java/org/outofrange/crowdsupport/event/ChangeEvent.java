package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ChangeEvent extends PayloadEvent<Object> {
    @JsonProperty
    ChangeType getChangeType();
}
