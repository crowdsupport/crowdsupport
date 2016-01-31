package org.outofrange.crowdsupport.service.impl;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.outofrange.crowdsupport.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class DonationRequestServiceImpl implements DonationRequestService {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestServiceImpl.class);

    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Inject
    private CommentService commentService;

    @Inject
    private WebSocketService webSocketService;

    @Inject
    private ModelMapper mapper;

    @Override
    public DonationRequest save(DonationRequest donationRequest) {
        return donationRequestRepository.save(donationRequest);
    }

    @Override
    public List<DonationRequest> loadAll() {
        return donationRequestRepository.findAll();
    }

    @Override
    public void setDonationRequestResolved(long id, boolean resolved) {
        log.debug("Setting resolved for donation request {} to {}", id, resolved);

        DonationRequest donationRequest = donationRequestRepository.findOne(id);
        donationRequest.setResolved(resolved);
        donationRequest = donationRequestRepository.save(donationRequest);

        webSocketService.sendChangeToPlace(ChangeDto.refresh(mapper.map(donationRequest, DonationRequestDto.class)),
                donationRequest.getPlace());
    }

    @Override
    public void deleteDonationRequest(long id) {
        log.debug("Deleting donation request with id {}", id);

        final DonationRequest donationRequest = donationRequestRepository.findOne(id);
        final Place place = donationRequest.getPlace();
        donationRequest.getComments().forEach(c -> commentService.deleteComment(c.getId()));

        donationRequestRepository.delete(id);

        webSocketService.sendChangeToPlace(ChangeDto.remove(id, DonationRequestDto.class), place);
    }
}
