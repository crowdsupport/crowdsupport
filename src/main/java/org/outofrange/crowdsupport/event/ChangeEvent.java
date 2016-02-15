package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A ChangeEvent indicates that an object has been changed
 */
public interface ChangeEvent extends PayloadEvent<Object> {
    /**
     * Returns the type of the change.
     * @return the type of the change
     */
    @JsonProperty
    ChangeType getChangeType();
}
