package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.PlaceRequestRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.Reflection;
import org.outofrange.crowdsupport.service.ServiceException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlaceRequestServiceImplTest {
    private PlaceRequestRepository placeRequestRepository;
    private CityRepository cityRepository;
    private PlaceService placeService;
    private UserService userService;

    private PlaceRequestServiceImpl placeRequestService;

    private final State state = new State("State name", "stateidentifier");
    private final City city = new City(state, "City name", "cityidentifier");
    private final User user = new User("username", "password");

    private Place place;
    private PlaceRequest placeRequest;

    @Before
    public void prepare() {
        placeRequestRepository = mock(PlaceRequestRepository.class);
        cityRepository = mock(CityRepository.class);
        placeService = mock(PlaceService.class);
        userService = mock(UserService.class);

        placeRequestService = new PlaceRequestServiceImpl(placeRequestRepository, cityRepository, placeService, userService);

        place = new Place(city, "Place name", "placeidentifier", "Place location");
        placeRequest = new PlaceRequest(place, user);

        when(placeRequestRepository.save(any(PlaceRequest.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(placeService.save(any(Place.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void declinePlaceRequestWorks() {
        when(placeRequestRepository.findOne(1L)).thenReturn(placeRequest);
        Reflection.setField(place, "id", 1L);

        placeRequestService.declinePlaceRequest(1);

        verify(placeRequestRepository).delete(placeRequest);
        verify(placeService).deletePlace(place.getId());
    }

    @Test(expected = ServiceException.class)
    public void decliningActivePlaceRequestThrowsException() {
        placeRequest.getPlace().setActive(true);

        when(placeRequestRepository.findOne(1L)).thenReturn(placeRequest);

        placeRequestService.declinePlaceRequest(1);
    }

    @Test(expected = ServiceException.class)
    public void declinePlaceRequestWithUnknownIdThrowsException() {
        when(placeRequestRepository.findOne(1L)).thenReturn(null);

        placeRequestService.declinePlaceRequest(1);
    }

    @Test
    public void loadAllReturnsResults() {
        when(placeRequestService.loadAll()).thenReturn(Collections.singletonList(placeRequest));

        assertEquals(1, placeRequestService.loadAll().size());
    }

    @Test
    public void loadAllReturnsNothing() {
        when(placeRequestService.loadAll()).thenReturn(Collections.emptyList());

        assertTrue(placeRequestService.loadAll().isEmpty());
    }

    @Test
    public void requestingNewPlaceWorks() {
        when(userService.getCurrentUserUpdated()).thenReturn(Optional.of(user));
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.of(city));

        placeRequestService.requestNewPlace(placeRequest);
    }

    @Test(expected = NullPointerException.class)
    public void requestingNewPlaceWithNullThrowsException() {
        placeRequestService.requestNewPlace(null);
    }

    @Test
    public void savingNewPlaceWorks() {
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.of(city));
        when(placeRequestRepository.findOne(1L)).thenReturn(placeRequest);
        when(placeService.load(state.getIdentifier(), city.getIdentifier(), place.getIdentifier())).thenReturn(Optional.of(place));

        Reflection.setField(placeRequest, "id", 1L);

        final PlaceRequest savedPlaceRequest = placeRequestService.saveNewPlace(placeRequest);

        assertTrue(savedPlaceRequest.getPlace().isActive());
        assertTrue(savedPlaceRequest.getPlace().getTeam().getMembers().contains(user));
        verify(placeRequestRepository).save(any(PlaceRequest.class));
    }

    @Test(expected = ServiceException.class)
    public void savingNewPlaceWithUnknownPlaceRequestThrowsException() {
        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.of(city));
        when(placeRequestRepository.findOne(1L)).thenReturn(null);
        when(placeService.load(state.getIdentifier(), city.getIdentifier(), place.getIdentifier())).thenReturn(Optional.of(place));
        Reflection.setField(placeRequest, "id", 1L);
        Reflection.setField(place, "id", 1L);

        placeRequestService.saveNewPlace(placeRequest);
    }

    @Test(expected = ServiceException.class)
    public void savingNewPlaceWithAlreadyExistingIdentifierThrowsException() {
        final Place exitingPlace = new Place(place.getCity(), place.getName(), place.getIdentifier(), place.getLocation());
        final PlaceRequest existingRequest = new PlaceRequest(exitingPlace, user);

        when(cityRepository.findOneByStateIdentifierAndIdentifier(state.getIdentifier(), city.getIdentifier()))
                .thenReturn(Optional.of(city));

        when(placeService.load(state.getIdentifier(), city.getIdentifier(), place.getIdentifier())).thenReturn(Optional.of(exitingPlace));
        when(placeRequestRepository.findOne(1L)).thenReturn(placeRequest);

        Reflection.setField(existingRequest, "id", 1L);
        Reflection.setField(placeRequest, "id", 2L);
        Reflection.setField(exitingPlace, "id", 1L);
        Reflection.setField(place, "id", 2L);

        placeRequestService.saveNewPlace(placeRequest);
    }
}