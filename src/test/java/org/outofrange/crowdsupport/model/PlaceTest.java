package org.outofrange.crowdsupport.model;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.util.IllegalArgumentStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlaceTest {
    private final State state = new State("State", "state");
    private final City city = new City(state, "Name", "identifier");

    private Place place;

    @Before
    public void prepare() {
        place = new Place(city, "Location", "Name", "identifier");
    }

    @Test(expected = NullPointerException.class)
    public void noCreationWithNullPlaceName() {
        new Place(city, "Location", null, "identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyPlaceName() {
        new Place(city, "Location", "", "identifier");
    }

    @Test(expected = NullPointerException.class)
    public void noCreationWithNullIdentifier() {
        new Place(city, "Location", "Name", null);
    }

    @Test(expected = NullPointerException.class)
    public void noCreationWithNullLocation() {
        new Place(city, null, "Name", "identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyLocation() {
        new Place(city, "", "Name", "identifier");
    }

    @Test
    public void noCreationWithInvalidIdentifier() {
        for (String illegalIdentifier : IllegalArgumentStore.ILLEGAL_IDENTIFIER_NAMES) {
            try {
                new Place(city, "Location", "Name", illegalIdentifier);
                throw new AssertionError("Constructor didn't threw an exception when passing in " + illegalIdentifier);
            } catch (IllegalArgumentException e) {
                // that's fine
            }
        }
    }

    @Test
    public void onlyIdentifierAndCity() {
        final Place p = new Place(city, "Location", "Name", "identifier");

        assertNotEquals(p, new Place(p.getCity(), p.getLocation(), p.getName(), "different"));
        assertNotEquals(p, new Place(new City(state, "Name", "id"), p.getLocation(), p.getName(), p.getIdentifier()));

        assertEquals(p, new Place(p.getCity(), p.getLocation(), "Name", p.getIdentifier()));
        assertEquals(p, new Place(p.getCity(), "different", p.getName(), p.getIdentifier()));
        assertEquals(p, new Place(p.getCity(), p.getLocation(), p.getName(), p.getIdentifier()));
    }

    @Test(expected = NullPointerException.class)
    public void settingNullCityThrowsAnException() {
        place.setCity(null);
    }

    @Test(expected = NullPointerException.class)
    public void settingNullRequestsThrowsAnException() {
        place.setDonationRequests(null);
    }
}