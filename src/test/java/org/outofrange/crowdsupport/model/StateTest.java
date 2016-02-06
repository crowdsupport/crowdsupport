package org.outofrange.crowdsupport.model;

import org.junit.Test;
import org.outofrange.crowdsupport.util.IllegalArgumentStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StateTest {
    @Test(expected = NullPointerException.class)
    public void noCreationWithNullStateName() {
        new State(null, "identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyStateName() {
        new State("", "identifier");
    }

    @Test(expected = NullPointerException.class)
    public void noCreationWithNullIdentifier() {
        new State("Name", null);
    }

    @Test
    public void noCreationWithInvalidIdentifier() {
        for (String illegalIdentifier : IllegalArgumentStore.ILLEGAL_IDENTIFIER_NAMES) {
            try {
                new State("Name", illegalIdentifier);
                throw new AssertionError("Constructor didn't threw an exception when passing in " + illegalIdentifier);
            } catch (IllegalArgumentException e) {
                // that's fine
            }
        }
    }

    @Test
    public void onlyIdentifierMattersForEqual() {
        final State s1 = new State("State", "identifier");
        final State s2 = new State("State", "differentidentifier");

        assertNotEquals(s1, s2);

        s2.setIdentifier(s1.getIdentifier());

        assertEquals(s1, s2);
    }

    @Test(expected = NullPointerException.class)
    public void settingNullCitiesThrowsAnException() {
        final State s = new State("Name", "identifier");

        s.setCities(null);
    }
}