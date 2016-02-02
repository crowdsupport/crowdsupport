package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
    Long countByResolved(boolean resolved);
}
