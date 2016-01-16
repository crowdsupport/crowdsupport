package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.service.StateService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    private static final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);

    @Inject
    private StateRepository stateRepository;

    @Override
    public State save(State state) {
        log.debug("Saving state {}", state);

        return stateRepository.save(state);
    }

    @Override
    public Optional<State> load(String identifier) {
        return stateRepository.findOneByIdentifier(identifier);
    }

    @Override
    public State createState(String name, String identifier, String imagePath) {
        if (load(identifier).isPresent()) {
            throw new ServiceException("There is already a state saved with the identifier " + identifier);
        }

        final State state = new State(name, identifier, imagePath);

        return save(state);
    }

    @Override
    public List<State> loadAll() {
        return stateRepository.findAll();
    }

    @Override
    public List<State> searchStates(String query) {
        return stateRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    public State saveOrRetrieveState(State state) {
        final Optional<State> loadedState = stateRepository.findOneByIdentifier(state.getIdentifier());

        if (loadedState.isPresent()) {
            if (loadedState.get().equals(state)) {
                return loadedState.get();
            } else {
                throw new ServiceException("Found different state with same identifier: " + loadedState.get());
            }
        } else {
            return save(state);
        }
    }
}
