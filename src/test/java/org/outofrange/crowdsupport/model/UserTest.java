package org.outofrange.crowdsupport.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {
    private User user;

    @Before
    public void prepare() {
        user = new User("user", "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingWithEmptyUsernameNotPossible() {
        new User("", "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingWithEmptyPasswordNotPossible() {
        new User("user", "");
    }

    @Test(expected = NullPointerException.class)
    public void creatingWithNullUsernameNotPossible() {
        new User(null, "password");
    }

    @Test(expected = NullPointerException.class)
    public void creatingWithNullPasswordNotPossible() {
        new User("user", null);
    }

    @Test
    public void passwordHasToBeHashedAfterCreation() {
        assertTrue(user.rehashPassword());
    }

    @Test
    public void passwordHasToBeHashedAgainAfterSetting() {
        user.setPasswordHash("password");
        user.setPassword("password");
        assertTrue(user.rehashPassword());
    }

    @Test
    public void passwordHasNotToBeHashedAfterSettingHash() {
        user.setPasswordHash("password");
        assertFalse(user.rehashPassword());
    }

    @Test
    public void getAuthoritiesReturnsRolesAndAuthoritiesSet() {
        final Permission p1 = new Permission("P1");
        final Permission p2 = new Permission("P2");

        final Role r1 = new Role("ROLE_1", p1, p2);
        final Role r2 = new Role("ROLE_2", p1);
        final List<Role> roles = new ArrayList<>();
        roles.add(r1);
        roles.add(r2);

        user.setRoles(roles);

        final Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertThat(authorities.size(), is(4));
        assertTrue(authorities.contains(p1));
        assertTrue(authorities.contains(p2));
        assertTrue(authorities.contains(r1));
        assertTrue(authorities.contains(r2));
    }
}