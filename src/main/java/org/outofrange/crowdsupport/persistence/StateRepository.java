package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    /**
     * Loads a state identifier by its identifier.
     *
     * @param identifier the identifier of the state.
     * @return the found state, or {@link Optional#empty()} if none could be found.
     */
    Optional<State> findOneByIdentifier(String identifier);

    /**
     * Loads all states where {@code namePart} can be found in their names, ignoring cases.
     *
     * @param namePart the text to look for.
     * @return a list of states with {@code namePart} in their names.
     */
    List<State> findAllByNameContainingIgnoreCase(String namePart);
}
