package org.outofrange.crowdsupport.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequestRepository, Long> {
}
