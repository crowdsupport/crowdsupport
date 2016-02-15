package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
    /**
     * Counts all donation requests by their {@code resolved} value.
     *
     * @param resolved the value of {@code resolved} that should be counted.
     * @return the number of counted donation requests.
     */
    Long countByResolved(boolean resolved);
}
