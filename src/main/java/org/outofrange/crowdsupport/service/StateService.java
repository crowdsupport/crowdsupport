package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Service
public class StateService {
    @Inject
    private StateRepository stateRepository;

    public List<State> getRecentlyUsedStates() {
        State s = new State("Austria");

        return Collections.singletonList(s);
    }
}
