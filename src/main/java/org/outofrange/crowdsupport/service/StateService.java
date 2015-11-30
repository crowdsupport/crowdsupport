package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class StateService {
    @Inject
    private StateRepository stateRepository;

    public State save(State state) {
        return stateRepository.save(state);
    }

    public Optional<State> load(String identifier) {
        return stateRepository.findOneByIdentifier(identifier);
    }

    public List<State> loadAllStates() {
        return stateRepository.findAll();
    }
}
