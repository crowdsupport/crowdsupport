package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.outofrange.crowdsupport.automation.keyword.ui.PlaceRequestKeywords;
import org.outofrange.crowdsupport.automation.keyword.ui.admin.AdminPlaceRequestKeywords;
import org.outofrange.crowdsupport.automation.keyword.ui.core.Sleeper;
import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.model.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class PlaceRequestUiTest extends UiTest {
    private static final String placeIdentifier = "testidentifier";

    @Test
    public void createPlaceInNewCityInNewState() {
        final User user = data().user().createUser(true);

        keywords().ui().sidePanel().login(user);
        PlaceRequestKeywords placeRequest = keywords().ui().sidePanel().gotoPlaceRequest();

        placeRequest
                .setCityAutocomplete("not existing")
                .clickToCityInputButton()
                .setCityName("City Name")
                .setStateName("State Name");

        placeRequest
                .setName("Place Test")
                .setIdentifier(placeIdentifier)
                .setLocation("Location");

        placeRequest.clickRequestButton();
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully requested place"));

        final AdminPlaceRequestKeywords.RequestedPlace requestedPlace =
                keywords().ui().sidePanel().gotoAdmin().gotoRequestedPlaces().forPlaceIdentifier(placeIdentifier);

        assertTrue(requestedPlace.isDisplayed());

        Sleeper.sleep(300);

        // state name should be set already
        requestedPlace.setStateIdentifier("stateidentifier");
        requestedPlace.createState();
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully created new state"));

        // city name should be set already
        requestedPlace.setCityIdentifier("cityidentifier");
        requestedPlace.createCity();
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully created new city"));

        requestedPlace.acceptRequest();

        Sleeper.sleep(500);
        assertThat(keywords().ui().status().getMessage(), containsString("Place successfully published"));
        assertFalse(requestedPlace.isDisplayed());
    }

    // problem with automating md-autocomplete
    @Ignore
    @Test
    public void createPlaceInNewCityInExistingState() {
        final User user = data().user().createUser(true);
        final State state = data().state().getState();

        keywords().ui().sidePanel().login(user);
        PlaceRequestKeywords placeRequest = keywords().ui().sidePanel().gotoPlaceRequest();

        placeRequest
                .setCityAutocomplete("not existing")
                .clickToCityInputButton()
                .setCityName("City Name")
                .setStateName(state.getName());

        placeRequest
                .setName("Place Test")
                .setIdentifier(placeIdentifier)
                .setLocation("Location");

        placeRequest.clickRequestButton();
        Sleeper.sleep(500);
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully requested place"));

        final AdminPlaceRequestKeywords.RequestedPlace requestedPlace =
                keywords().ui().sidePanel().gotoAdmin().gotoRequestedPlaces().forPlaceIdentifier(placeIdentifier);

        assertTrue(requestedPlace.isDisplayed());

        Sleeper.sleep(300);

        requestedPlace.clickToStateSearch();
        requestedPlace.setStateAutocomplete(state.getName());

        // city name should be set already
        requestedPlace.setCityIdentifier("cityidentifier");

        assertTrue(requestedPlace.isCreateCityButtonEnabled());
        requestedPlace.createCity();
        Sleeper.sleep(500);
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully created new city"));

        requestedPlace.acceptRequest();

        assertThat(keywords().ui().status().getMessage(), containsString("Place successfully published"));
        assertFalse(requestedPlace.isDisplayed());
    }

    @Test
    public void createPlaceInExistingCity() {
        final User user = data().user().createUser(true);
        final City city = data().city().getCity();

        keywords().ui().sidePanel().login(user);
        PlaceRequestKeywords placeRequest = keywords().ui().sidePanel().gotoPlaceRequest();

        placeRequest
                .setName("Place Test")
                .setIdentifier(placeIdentifier)
                .setCityAutocomplete(city.getName())
                .setLocation("Location")
                .clickRequestButton();

        Sleeper.sleep(500);
        assertThat(keywords().ui().status().getMessage(), containsString("Successfully requested place"));

        final AdminPlaceRequestKeywords.RequestedPlace requestedPlace =
                keywords().ui().sidePanel().gotoAdmin().gotoRequestedPlaces().forPlaceIdentifier(placeIdentifier);

        assertTrue(requestedPlace.isDisplayed());

        requestedPlace.declineRequest();

        assertThat(keywords().ui().status().getMessage(), containsString("successfully declined"));
        assertFalse(requestedPlace.isDisplayed());
    }
}
