package org.outofrange.crowdsupport.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PermissionTest {
    @Test(expected = NullPointerException.class)
    public void noCreationWithNull() {
        new Permission(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyPermissionName() {
        new Permission("");
    }
    @Test
    public void creationWillUppercaseTheName() {
        final Permission role = new Permission("perm");

        assertThat(role.getName(), is(equalTo("PERM")));
        assertThat(role.getAuthority(), is(equalTo("PERM")));
    }

    @Test
    public void noSpecialCharactersAllowed() {
        try {
            new Permission("PERM ISSION");
            throw new AssertionError("No special characters allowed");
        } catch (IllegalArgumentException e) {
            // everything alright
        }
    }

    @Test
    public void permissionsWithSameNameAreEqual() {
        final Permission p1 = new Permission("PERM");
        final Permission p2 = new Permission("PERM");

        assertEquals(p1, p2);
    }

    @Test
    public void permissionsWithDifferentNamesArentEqual() {
        final Permission p1 = new Permission("PERM_1");
        final Permission p2 = new Permission("PERM_2");

        assertNotEquals(p1, p2);
    }
}