package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.service.ServiceException;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CityServiceImplTest {
    private final State state = new State("State Name", "stateidentifier");
    private final City city = new City(state, "City Name", "cityidentifier");

    private CityRepository cityRepository;
    private StateRepository stateRepository;

    private CityServiceImpl cityService;

    @Before
    public void prepare() {
        cityRepository = mock(CityRepository.class);
        stateRepository = mock(StateRepository.class);

        cityService = new CityServiceImpl(cityRepository, stateRepository);
    }

    @Test
    public void loadingKnownId() {
        when(cityRepository.findOne(1L)).thenReturn(city);

        assertNotNull(cityService.load(1));
    }

    @Test
    public void loadingUnkownIdReturnsNothing() {
        when(cityRepository.findOne(1L)).thenReturn(null);

        assertNull(cityService.load(1));
    }

    @Test(expected = NullPointerException.class)
    public void loadWithNullIdentifierThrowsException() {
        cityService.load(null, state.getIdentifier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadWithEmptyIdentifierThrowsException() {
        cityService.load("", state.getIdentifier());
    }

    @Test(expected = NullPointerException.class)
    public void loadWithNullStateIdentifierThrowsException() {
        cityService.load(city.getIdentifier(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadWithEmptyStateIdentifierThrowsException() {
        cityService.load(city.getIdentifier(), "");
    }

    @Test
    public void loadWithUnkownStateIdentifierReturnsNothing() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.empty());
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.empty());

        assertFalse(cityService.load(city.getIdentifier(), state.getIdentifier()).isPresent());
    }

    @Test
    public void loadWithUnkownCityIdentifierReturnsNothing() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.of(state));
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.empty());

        assertFalse(cityService.load(city.getIdentifier(), state.getIdentifier()).isPresent());
    }

    @Test(expected = ServiceException.class)
    public void createCityWithUnkownStateIdentifierThrowsException() {
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.empty());
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.empty());

        cityService.createCity(city.getName(), city.getIdentifier(), city.getImagePath(), state.getIdentifier());
    }

    @Test(expected = ServiceException.class)
    public void createCityWithAlreadyExistingIdentifierThrowsException() {
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.of(city));

        cityService.createCity(city.getName(), city.getIdentifier(), city.getImagePath(), state.getIdentifier());
    }

    @Test
    public void loadAllReturnsNothing() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(0, is(equalTo(cityService.loadAll().size())));
    }

    @Test
    public void loadAllReturnsSomething() {
        when(cityRepository.findAll()).thenReturn(Collections.singletonList(city));

        assertThat(1, is(equalTo(cityService.loadAll().size())));
    }

    @Test(expected = NullPointerException.class)
    public void queryWithNullThrowsException() {
        cityService.searchCities(null);
    }

    @Test
    public void queryReturnsResults() {
        when(cityRepository.findAllByNameContainingIgnoreCase(city.getName())).thenReturn(Collections.singletonList(city));

        assertThat(1, is(equalTo(cityService.searchCities(city.getName()).size())));
    }
}