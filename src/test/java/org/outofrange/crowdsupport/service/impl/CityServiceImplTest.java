package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.StateRepository;

import static org.mockito.Mockito.*;

public class CityServiceImplTest {
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
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadingUnkownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithNullIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithEmptyIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithNullStateIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithEmptyStateIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnkownStateIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnkownCityIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void createCityWithUnkownStateIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void createCityWithAlreadyExistingIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsMultipleCities() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void queryWithNull() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void queryWithEmpty() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void queryReturnsMultipleResults() {
        throw new AssertionError("Not yet implemented");
    }
}