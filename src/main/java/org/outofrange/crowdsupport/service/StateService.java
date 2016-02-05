package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService {
    Optional<State> load(String identifier);

    State load(long id);

    State createState(String name, String identifier, String imagepath);

    List<State> searchStates(String query);

    List<State> loadAll();
}
