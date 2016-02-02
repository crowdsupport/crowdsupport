package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClientEvent extends Event {
    @JsonIgnore
    String getTopic();
}
