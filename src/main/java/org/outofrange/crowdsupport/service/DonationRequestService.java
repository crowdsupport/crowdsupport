package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;

import java.util.Optional;

public interface DonationRequestService extends BaseService<DonationRequest> {
    Optional<DonationRequest> loadWithId(long id);
}
