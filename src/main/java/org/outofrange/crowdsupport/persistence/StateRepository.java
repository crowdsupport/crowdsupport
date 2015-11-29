package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
