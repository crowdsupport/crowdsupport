package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.dto.StatisticsDto;
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

    @Before
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
        when(stateRepository.count()).thenReturn(1L);
        when(cityRepository.count()).thenReturn(2L);
        when(placeRepository.count()).thenReturn(3L);
        when(donationRequestRepository.countByResolved(false)).thenReturn(4L);
        when(donationRequestRepository.countByResolved(true)).thenReturn(5L);
        when(userRepository.count()).thenReturn(6L);

        final StatisticsDto dto = statisticsService.getStatistics();

        assertThat(1L, is(equalTo(dto.getStates())));
        assertThat(2L, is(equalTo(dto.getCities())));
        assertThat(3L, is(equalTo(dto.getPlaces())));
        assertThat(4L, is(equalTo(dto.getOpenRequests())));
        assertThat(5L, is(equalTo(dto.getClosedRequests())));
        assertThat(6L, is(equalTo(dto.getTotalUsers())));
    }

    @Test
    public void getStatisticsWithEverything0() {
        when(stateRepository.count()).thenReturn(0L);
        when(cityRepository.count()).thenReturn(0L);
        when(placeRepository.count()).thenReturn(0L);
        when(donationRequestRepository.countByResolved(false)).thenReturn(0L);
        when(donationRequestRepository.countByResolved(true)).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);

        final StatisticsDto dto = statisticsService.getStatistics();

        assertThat(0L, is(equalTo(dto.getStates())));
        assertThat(0L, is(equalTo(dto.getCities())));
        assertThat(0L, is(equalTo(dto.getPlaces())));
        assertThat(0L, is(equalTo(dto.getOpenRequests())));
        assertThat(0L, is(equalTo(dto.getClosedRequests())));
        assertThat(0L, is(equalTo(dto.getTotalUsers())));
    }
}