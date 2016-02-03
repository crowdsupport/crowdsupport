package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.StatisticsDto;
import org.outofrange.crowdsupport.persistence.*;
import org.outofrange.crowdsupport.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Inject
    private StateRepository stateRepository;

    @Inject
    private CityRepository cityRepository;

    @Inject
    private PlaceRepository placeRepository;

    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    public StatisticsDto getStatistics() {
        log.debug("Querying data for statistics");

        final StatisticsDto dto = new StatisticsDto();
        dto.setStates(stateRepository.count());
        dto.setCities(cityRepository.count());
        dto.setPlaces(placeRepository.count());

        dto.setOpenRequests(donationRequestRepository.countByResolved(false));
        dto.setClosedRequests(donationRequestRepository.countByResolved(true));

        dto.setTotalUsers(userRepository.count());

        return dto;
    }
}
