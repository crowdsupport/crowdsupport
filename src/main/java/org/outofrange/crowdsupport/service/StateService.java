package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService extends BaseService<State> {
    Optional<State> load(String identifier);

    State createState(String name, String identifier, String imagepath);

    List<State> searchStates(String query);

    State saveOrRetrieveState(State state);
}
