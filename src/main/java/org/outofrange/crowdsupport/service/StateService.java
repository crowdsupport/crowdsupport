package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService extends CrowdsupportService<State> {
    Optional<State> load(String identifier);
}
