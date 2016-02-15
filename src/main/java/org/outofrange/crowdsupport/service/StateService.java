package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService {
    /**
     * Loads a state.
     *
     * @param identifier the identifier of the state to load
     * @return the loaded state, or {@link Optional#empty()} if none could be found
     */
    Optional<State> load(String identifier);

    /**
     * Loads a state.
     *
     * @param id the id of the state to load
     * @return the loaded state
     */
    State load(long id);

    /**
     * Creates a new state.
     *
     * @param name       the name of the state
     * @param identifier the identifier of the state
     * @param imagePath  the imagepath of the state
     * @return the newly created state
     * @throws ServiceException if there is already an existing state using the same identifier
     */
    State createState(String name, String identifier, String imagePath);

    /**
     * Loads all states having {@code query} in their names. Ignores case.
     *
     * @param query the text to look for
     * @return all matchin states
     */
    List<State> searchStates(String query);

    /**
     * Loads all persisted states.
     *
     * @return all persisted states
     */
    List<State> loadAll();
}
