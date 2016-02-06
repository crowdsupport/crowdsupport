package org.outofrange.crowdsupport.service.impl;

import org.junit.Test;
import org.outofrange.crowdsupport.persistence.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class StatisticsServiceImplTest {
    private StateRepository stateRepository;
    private CityRepository cityRepository;
    private PlaceRepository placeRepository;
    private DonationRequestRepository donationRequestRepository;
    private UserRepository userRepository;

    private StatisticsServiceImpl statisticsService;

    public void prepare() {
        stateRepository = mock(StateRepository.class);
        cityRepository = mock(CityRepository.class);
        placeRepository = mock(PlaceRepository.class);
        donationRequestRepository = mock(DonationRequestRepository.class);
        userRepository = mock(UserRepository.class);

        statisticsService = new StatisticsServiceImpl(stateRepository, cityRepository, placeRepository,
                donationRequestRepository, userRepository);
    }

    @Test
    public void getStatisticsWithResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void getStatisticsWithEverything0() {
        throw new AssertionError("Not yet implemented");
    }
}