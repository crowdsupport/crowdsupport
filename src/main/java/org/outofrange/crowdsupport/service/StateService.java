package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;

import java.util.Optional;

public interface StateService extends BaseService<State> {
    Optional<State> load(String identifier);
}
