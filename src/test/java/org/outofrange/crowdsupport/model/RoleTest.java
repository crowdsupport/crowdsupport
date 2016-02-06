package org.outofrange.crowdsupport.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class RoleTest {
    private Permission p1 = new Permission("P1");
    private Permission p2 = new Permission("P2");

    @Test(expected = NullPointerException.class)
    public void noCreationWithNull() {
        new Role(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmptyRoleName() {
        new Role("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithROLE_() {
        new Role("ROLE_");
    }

    @Test
    public void creationWithoutAStartingROLE_WillPrefixIt() {
        final Role role = new Role("TE");

        assertThat(role.getName(), is(equalTo("ROLE_TE")));
        assertThat(role.getAuthority(), is(equalTo("ROLE_TE")));
    }

    @Test
    public void creationWithoutAStartingROLE_AndMoreCharactersWillPrefixIt() {
        final Role role = new Role("TESTTEST");

        assertThat(role.getName(), is(equalTo("ROLE_TESTTEST")));
        assertThat(role.getAuthority(), is(equalTo("ROLE_TESTTEST")));
    }

    @Test
    public void creationWillUppercaseTheName() {
        final Role role = new Role("role_test");

        assertThat(role.getName(), is(equalTo("ROLE_TEST")));
        assertThat(role.getAuthority(), is(equalTo("ROLE_TEST")));
    }

    @Test
    public void creationWithPermissions() {
        final Role role = new Role("ROLE_TEST", p1, p2);

        final Set<Permission> permissions = role.getPermissions();
        assertThat(2, is(equalTo(permissions.size())));
        assertTrue(permissions.containsAll(Arrays.asList(p1, p2)));
    }

    @Test
    public void creationWithMultiplePermissionsCreatesNoDuplicates() {
        final Role role = new Role("ROLE_TEST", p1, p2, new Permission(p1.getName()));

        final Set<Permission> permissions = role.getPermissions();
        assertThat(2, is(equalTo(permissions.size())));
        assertTrue(permissions.containsAll(Arrays.asList(p1, p2)));
    }

    @Test
    public void noSpecialCharactersAllowed() {
        try {
            new Role("ROLE TEST");
            throw new AssertionError("No special characters allowed");
        } catch (IllegalArgumentException e) {
            // everything alright
        }
    }

    @Test
    public void rolesWithSameNameAndDifferentPermissionsAreEqual() {
        final Role r1 = new Role("ROLE_TEST", p1);
        final Role r2 = new Role("ROLE_TEST", p1, p2);

        assertEquals(r1, r2);
    }

    @Test
    public void rolesWithDifferentNamesAndSamePermissionsArentEqual() {
        final Role r1 = new Role("ROLE_R1", p1);
        final Role r2 = new Role("ROLE_R2", p1);

        assertNotEquals(r1, r2);
    }
}