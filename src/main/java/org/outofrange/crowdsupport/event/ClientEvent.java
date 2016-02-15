package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * A ClientEvent is an event which should be sent over the WebSocket.
 */
public interface ClientEvent extends Event {
    /**
     * Returns all topics the ClientEvent should be published to.
     *
     * @return a list of topics
     */
    @JsonIgnore
    List<String> getTopics();
}
