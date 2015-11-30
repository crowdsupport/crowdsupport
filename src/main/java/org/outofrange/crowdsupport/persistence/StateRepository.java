package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findOneByIdentifier(String identifier);
}
