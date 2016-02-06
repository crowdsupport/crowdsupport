package org.outofrange.crowdsupport.model;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.util.IllegalArgumentStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CityTest {
    private final State state = new State("Name", "identifier");
    
    private City city;
    
    @Before
    public void prepare() {
        city = new City(state, "Name", "identifier");
    }
    
    @Test(expected = NullPointerException.class)
    public void noCreationWithNullCityName() {
        new City(state, null, "identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyCityName() {
        new City(state, "", "identifier");
    }

    @Test(expected = NullPointerException.class)
    public void noCreationWithNullIdentifier() {
        new City(state, "Name", null);
    }

    @Test
    public void noCreationWithInvalidIdentifier() {
        for (String illegalIdentifier : IllegalArgumentStore.ILLEGAL_IDENTIFIER_NAMES) {
            try {
                new City(state, "Name", illegalIdentifier);
                throw new AssertionError("Constructor didn't threw an exception when passing in " + illegalIdentifier);
            } catch (IllegalArgumentException e) {
                // that's fine
            }
        }
    }

    @Test
    public void onlyIdentifierMattersForEqual() {
        final City c1 = new City(state, "City", "identifier");
        final City c2 = new City(state, "City", "differentidentifier");

        assertNotEquals(c1, c2);

        c2.setIdentifier(c1.getIdentifier());

        assertEquals(c1, c2);
    }

    @Test(expected = NullPointerException.class)
    public void settingNullStateThrowsAnException() {
        city.setState(null);
    }

    @Test(expected = NullPointerException.class)
    public void settingNullPlacesThrowsAnException() {
        city.setPlaces(null);
    }
}