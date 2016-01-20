package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
