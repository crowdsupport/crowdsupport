package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;

import java.util.List;

public interface DonationRequestService {
    List<DonationRequest> loadAll();

    void setDonationRequestResolved(long id, boolean resolved);

    void deleteDonationRequest(long id);
}
