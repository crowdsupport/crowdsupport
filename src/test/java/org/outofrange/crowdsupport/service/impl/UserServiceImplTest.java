package org.outofrange.crowdsupport.service.impl;

import org.junit.Test;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.util.ServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    private UserServiceImpl userService;

    public void prepare() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        roleRepository = mock(RoleRepository.class);

        userService = new UserServiceImpl(userRepository, passwordEncoder, roleRepository);
    }

    @Test
    public void creatingUserWithInvalidDtoThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void creatingUserWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void creatingUserWithNullDtoThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void getCurrentUserWhenUserLoggedIn() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void getCurrentUserWhenNoUserLoggedIn() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = ServiceException.class)
    public void getCurrentUserUpdatedWhenUserLoggedInButWasDeletedInDb() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void getCurrentUserUpdatedWhenUserLoggedIn() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void getCurrentUserUpdatedWhenNoUserLoggedIn() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsSomething() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsEmptyList() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadUserWithKnownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadUserWithUnknownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadUserByNameReturnsUserWhenFound() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByNameThrowsExceptionWhenUserNotFound() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void loadUserByNameWithNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void queryUsersWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void queryUsersWithNoResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void queryUsersWithNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void updateAllWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void updateAllWithDtoNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void updateAllWithMissingUser() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void updateAllSettingNameToExistingUser() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void updateProfileWithCurrentUserWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = ServiceException.class)
    public void updateProfileWithNoCurrentUserThrowsException() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void updateProfileWithDtoNullThrowsException() {
        throw new AssertionError("Not yet implemented");
    }
}