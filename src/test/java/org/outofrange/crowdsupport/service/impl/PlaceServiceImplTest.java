package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.outofrange.crowdsupport.persistence.TeamRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.util.Reflection;
import org.outofrange.crowdsupport.util.ServiceException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlaceServiceImplTest {
    private PlaceRepository placeRepository;
    private DonationRequestRepository donationRequestRepository;
    private UserRepository userRepository;
    private TeamRepository teamRepository;

    private PlaceServiceImpl placeService;

    private final State state = new State("State name", "stateidentifier");
    private final City city = new City(state, "City name", "cityidentifier");
    private final User user = new User("username", "password");

    private Place place;
    private DonationRequest donationRequest;

    @Before
    public void prepare() {
        placeRepository = mock(PlaceRepository.class);
        donationRequestRepository = mock(DonationRequestRepository.class);
        userRepository = mock(UserRepository.class);
        teamRepository = mock(TeamRepository.class);

        placeService = new PlaceServiceImpl(placeRepository, donationRequestRepository, userRepository, teamRepository);

        place = new Place(city, "Place name", "placeidentifier", "Location");
        place.setTeam(new Team(place));
        Reflection.setField(place, "id", 1L);
        donationRequest = new DonationRequest(place, "Title", "Description");

        when(donationRequestRepository.save(any(DonationRequest.class))).then(invocation -> invocation.getArguments()[0]);
        when(teamRepository.save(any(Team.class))).then(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void addingDonationRequestToKnownPlaceWorks() {
        when(placeRepository.findOne(1L)).thenReturn(place);

        placeService.addDonationRequest(1, donationRequest);

        verify(donationRequestRepository).save(any(DonationRequest.class));
    }

    @Test(expected = ServiceException.class)
    public void addingDonationRequestToUnknownPlaceThrowsException() {
        when(placeRepository.findOne(1L)).thenReturn(null);

        placeService.addDonationRequest(1, donationRequest);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullDonationRequestThrowsException() {
        placeService.addDonationRequest(1, null);
    }

    @Test
    public void deletingKnownPlaceWorks() {
        when(placeRepository.findOne(1L)).thenReturn(place);

        placeService.deletePlace(1);

        verify(placeRepository).delete(1L);
    }

    @Test
    public void loadWithEitherNullOrEmptyValuesThrowsException() {
        final PlaceService s = placeService;

        try { s.load(null, "a", "a"); throw new AssertionError(); } catch (NullPointerException e) { /* k */ }
        try { s.load("a", null, "a"); throw new AssertionError(); } catch (NullPointerException e) { /* k */ }
        try { s.load("a", "a", null); throw new AssertionError(); } catch (NullPointerException e) { /* k */ }

        try { s.load("", "a", "a"); throw new AssertionError(); } catch (IllegalArgumentException e) { /* k */ }
        try { s.load("a", "", "a"); throw new AssertionError(); } catch (IllegalArgumentException e) { /* k */ }
        try { s.load("a", "a", ""); throw new AssertionError(); } catch (IllegalArgumentException e) { /* k */ }
    }

    @Test
    public void loadAllReturnsResults() {
        when(placeRepository.findAll()).thenReturn(Collections.singletonList(place));

        assertEquals(1, placeService.loadAll().size());
    }

    @Test
    public void loadAllReturnsNothing() {
        when(placeRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(placeService.loadAll().isEmpty());
    }

    @Test
    public void loadPlaceWithExistingId() {
        when(placeRepository.findOne(1L)).thenReturn(place);

        assertNotNull(placeService.loadPlace(1));
    }

    @Test
    public void addingUserToTeamTheyAreAlreadyInDoesNothing() {
        place.getTeam().getMembers().add(user);
        when(placeRepository.findOne(1L)).thenReturn(place);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        placeService.addUserToTeam(1, user.getUsername());

        verifyZeroInteractions(teamRepository);
    }

    @Test
    public void addingUserToExistingTeamWorks() {
        when(placeRepository.findOne(1L)).thenReturn(place);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        final Place updatedPlace = placeService.addUserToTeam(1, user.getUsername());

        assertTrue(updatedPlace.getTeam().getMembers().contains(user));

        verify(teamRepository).save(any(Team.class));
    }

    @Test(expected = ServiceException.class)
    public void addingUserToUnknownPlaceThrowsException() {
        when(placeRepository.findOne(1L)).thenReturn(null);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        placeService.addUserToTeam(1, user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingUserToTeamWithEmptyUsernameThrowsException() {
        placeService.addUserToTeam(1, "");
    }

    @Test(expected = NullPointerException.class)
    public void addingUserToTeamWithNullUserThrowsException() {
        placeService.addUserToTeam(1, null);
    }

    @Test
    public void removingUserFromTeamTheyAreNotInDoesNothing() {
        when(placeRepository.findOne(1L)).thenReturn(place);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        placeService.removeUserFromTeam(1, user.getUsername());

        verifyZeroInteractions(teamRepository);
    }

    @Test
    public void removingUserWorks() {
        place.getTeam().getMembers().add(user);
        when(placeRepository.findOne(1L)).thenReturn(place);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        final Place updatedPlace = placeService.removeUserFromTeam(1, user.getUsername());

        verify(teamRepository).save(any(Team.class));
        assertFalse(updatedPlace.getTeam().getMembers().contains(user));
    }

    @Test(expected = ServiceException.class)
    public void removingUserFromUnknownPlaceThrowsException() {
        when(placeRepository.findOne(1L)).thenReturn(null);
        when(userRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));

        placeService.removeUserFromTeam(1, user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingUserFromTeamWithEmptyUsernameThrowsException() {
        placeService.removeUserFromTeam(1, "");
    }

    @Test(expected = NullPointerException.class)
    public void removingUserFromTeamWithNullUsernameThrowsException() {
        placeService.removeUserFromTeam(1, null);
    }
}