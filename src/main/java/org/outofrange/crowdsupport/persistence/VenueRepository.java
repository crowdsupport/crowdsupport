package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
}
