package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.outofrange.crowdsupport.persistence.TeamRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class PlaceServiceImplTest {
    private PlaceRepository placeRepository;
    private DonationRequestRepository donationRequestRepository;
    private UserRepository userRepository;
    private TeamRepository teamRepository;

    private PlaceServiceImpl placeService;

    @Before
    public void prepare() {
        placeRepository = mock(PlaceRepository.class);
        donationRequestRepository = mock(DonationRequestRepository.class);
        userRepository = mock(UserRepository.class);
        teamRepository = mock(TeamRepository.class);

        placeService = new PlaceServiceImpl(placeRepository, donationRequestRepository, userRepository, teamRepository);
    }

    @Test
    public void addingDonationRequestToKnownPlaceWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingDonationRequestToUnkownPlaceThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingNullDonationRequestThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deletingKnownStateWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deletingUnknownStateDoesNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnkownPlaceReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnkownCityReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnkownStateReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithEitherNullOrEmptyValuesThrowsException() {
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
    public void loadPlaceWithUnkownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadPlaceWithExistingId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingUserToTeamTheyAreAlreadyInDoesNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingUserToExistingTeamWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingUserToUnkownTeamThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingUserToTeamWithEmptyThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingUserToTeamWithNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void removingUserFromTeamTheyAreNotInDoesNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void removingUserWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void removingUserFromUnkownTeamThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void removingUserFromTeamWithEmptyUsernameThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void removingUserFromTeamWithNullUsernameThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingNewPlace() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void savingExistingPlace() {
        throw new AssertionError("Not yet implemented");
    }
}