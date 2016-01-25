package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;

import java.util.List;

public interface DonationRequestService {
    DonationRequest save(DonationRequest entity);

    List<DonationRequest> loadAll();
}
