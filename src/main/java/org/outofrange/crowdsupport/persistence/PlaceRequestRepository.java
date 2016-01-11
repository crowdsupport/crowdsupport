package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.PlaceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRequestRepository extends JpaRepository<PlaceRequest, Long> {
}
