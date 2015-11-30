package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class DonationRequestService {
    @Inject
    private DonationRequestRepository donationRequestRepository;

    public DonationRequest save(DonationRequest donationRequest) {
        return donationRequestRepository.save(donationRequest);
    }
}
