package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.PlaceRequestRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.service.UserService;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class PlaceRequestServiceImplTest {
    private PlaceRequestRepository placeRequestRepository;
    private CityRepository cityRepository;
    private PlaceService placeService;
    private UserService userService;

    private PlaceRequestServiceImpl placeRequestService;

    @Before
    public void prepare() {
        placeRequestRepository = mock(PlaceRequestRepository.class);
        cityRepository = mock(CityRepository.class);
        placeService = mock(PlaceService.class);
        userService = mock(UserService.class);

        placeRequestService = new PlaceRequestServiceImpl(placeRequestRepository, cityRepository, placeService, userService);
    }

    @Test
    public void declinePlaceRequestWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void declinePlaceRequestWithUnkownIdDoesNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadingAllWithInactivePlacesIgnoresActivePlaces() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void requestingNewPlaceWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void requestingNewPlaceWithNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlaceWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlaceWithUnkownPlaceThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlaceWithUnkownPlaceRequestThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlaceWithNullPlaceRequestThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlaceWithAleadyExistingIdentifierThrowsException() {
        throw new AssertionError("Not yet implemented");
    }
}