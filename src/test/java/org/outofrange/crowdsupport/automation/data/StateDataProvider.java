package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.StateService;
import org.outofrange.crowdsupport.util.Authorized;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

@Component
public class StateDataProvider {
    private static final String DEFAULT_NAME = "State Name";
    private static final String DEFAULT_IDENTIFIER = "stateid";

    private final StateService stateService;

    @Inject
    public StateDataProvider(StateService stateService) {
        this.stateService = stateService;
    }

    public State getState() {
        Optional<State> state = stateService.load(DEFAULT_IDENTIFIER);

        if (state.isPresent()) {
            return state.get();
        } else {

            State newState = Authorized.asAdmin().run(() -> stateService.createState(DEFAULT_NAME, DEFAULT_IDENTIFIER, null));
            DataProvider.registerUndo(() -> stateService.deleteState(newState.getId()));

            return newState;
        }
    }
}
