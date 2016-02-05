package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DonationRequestServiceImpl implements DonationRequestService {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestServiceImpl.class);

    private final DonationRequestRepository donationRequestRepository;
    private final CommentService commentService;

    @Inject
    public DonationRequestServiceImpl(DonationRequestRepository donationRequestRepository, CommentService commentService) {
        this.donationRequestRepository = donationRequestRepository;
        this.commentService = commentService;
    }

    @Override
    public List<DonationRequest> loadAll() {
        return donationRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public void setDonationRequestResolved(long id, boolean resolved) {
        log.debug("Setting resolved for donation request {} to {}", id, resolved);

        DonationRequest donationRequest = donationRequestRepository.findOne(id);
        donationRequest.setResolved(resolved);
        donationRequest = donationRequestRepository.save(donationRequest);

        Events.donationRequest(ChangeType.UPDATE, donationRequest).publish();
        Events.donationRequestConfirmed().publish();
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteDonationRequest(long id) {
        log.debug("Deleting donation request with id {}", id);

        final DonationRequest donationRequest = donationRequestRepository.findOne(id);
        donationRequest.getComments().forEach(c -> commentService.deleteComment(c.getId()));

        donationRequestRepository.delete(id);

        Events.donationRequest(ChangeType.DELETE, donationRequest).publish();
    }
}
