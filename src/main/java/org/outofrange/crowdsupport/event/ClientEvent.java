package org.outofrange.crowdsupport.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface ClientEvent extends Event {
    @JsonIgnore
    List<String> getTopics();
}
