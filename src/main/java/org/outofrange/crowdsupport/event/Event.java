package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Event {
    /**
     * The name of the event; default is the classname.
     *
     * @return the name of the event
     */
    @JsonProperty
    default String name() {
        return getClass().getSimpleName();
    }

    /**
     * Publish the event through {@link EventDispatcher}
     */
    default void publish() {
        EventDispatcher.post(this);
    }
}
