package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.service.StateService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    @Inject
    private StateRepository stateRepository;

    @Override
    public State save(State state) {
        return stateRepository.save(state);
    }

    @Override
    public Optional<State> load(String identifier) {
        return stateRepository.findOneByIdentifier(identifier);
    }

    @Override
    public List<State> loadAll() {
        return stateRepository.findAll();
    }

    @Override
    public List<State> searchStates(String query) {
        return stateRepository.findAllByNameContainingIgnoreCase(query);
    }
}
