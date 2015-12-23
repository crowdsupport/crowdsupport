package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class DonationRequestServiceImpl implements DonationRequestService {
    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Override
    public DonationRequest save(DonationRequest donationRequest) {
        return donationRequestRepository.save(donationRequest);
    }

    @Override
    public List<DonationRequest> loadAll() {
        return donationRequestRepository.findAll();
    }
}