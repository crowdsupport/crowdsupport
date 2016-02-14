package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.util.Reflection;
import org.outofrange.crowdsupport.util.RoleStore;
import org.outofrange.crowdsupport.util.ServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private CurrentUserProvider currentUserProvider;

    private UserServiceImpl userService;

    private User user;
    private FullUserDto userDto;

    @Before
    public void prepare() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        roleRepository = mock(RoleRepository.class);
        currentUserProvider = mock(CurrentUserProvider.class);

        userService = new UserServiceImpl(userRepository, passwordEncoder, roleRepository);
        userService.setCurrentUserProvider(currentUserProvider);

        user = new User("username", "password");
        userDto = new FullUserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingUserWithInvalidDtoThrowsException() {
        userDto.setUsername("");

        userService.createUser(userDto);
    }

    @Test
    public void creatingUserWorks() {
        when(roleRepository.findOneByName(RoleStore.USER)).thenReturn(Optional.of(new Role(RoleStore.USER)));
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        User createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertTrue(createdUser.getRoles().contains(new Role(RoleStore.USER)));
        assertThat("hashed", is(equalTo(createdUser.getPassword())));
        assertFalse(createdUser.rehashPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test(expected = ServiceException.class)
    public void creatingUserWithExistingUsernameThrowsException() {
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.of(user));

        userService.createUser(userDto);
    }

    @Test(expected = NullPointerException.class)
    public void creatingUserWithNullDtoThrowsException() {
        userService.createUser(null);
    }

    @Test
    public void getCurrentUserWhenUserLoggedIn() {
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));

        assertThat(user.getUsername(), is(equalTo(userService.getCurrentUsername().get())));
    }

    @Test
    public void getCurrentUserWhenNoUserLoggedIn() {
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.empty());

        assertFalse(userService.getCurrentUsername().isPresent());
    }

    @Test(expected = ServiceException.class)
    public void getCurrentUserUpdatedWhenUserLoggedInButWasDeletedInDb() {
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.empty());

        userService.getCurrentUserUpdated();
    }

    @Test
    public void getCurrentUserUpdatedWhenUserLoggedIn() {
        User updatedUser = new User(user.getUsername(), user.getPassword());
        updatedUser.setEmail("some@mail.com");

        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.of(updatedUser));

        assertThat(updatedUser.getEmail(), is(equalTo(userService.getCurrentUserUpdated().get().getEmail())));
    }

    @Test
    public void getCurrentUserUpdatedWhenNoUserLoggedIn() {
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.empty());

        assertFalse(userService.getCurrentUserUpdated().isPresent());

        verifyZeroInteractions(userRepository);
    }

    @Test
    public void loadAllReturnsSomething() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        assertThat(1, is(equalTo(userService.loadAll().size())));
    }

    @Test
    public void loadAllReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(userService.loadAll().isEmpty());
    }

    @Test
    public void loadUserWithKnownId() {
        when(userRepository.findOne(1L)).thenReturn(user);

        assertNotNull(userService.loadUser(1));
    }

    @Test
    public void loadUserWithUnknownId() {
        when(userRepository.findOne(1L)).thenReturn(null);

        assertNull(userService.loadUser(1));
    }

    @Test
    public void loadUserByNameReturnsUserWhenFound() {
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.of(user));

        assertNotNull(userService.loadUserByUsername(user.getUsername()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByNameThrowsExceptionWhenUserNotFound() {
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.empty());

        userService.loadUserByUsername(user.getUsername());
    }

    @Test(expected = NullPointerException.class)
    public void loadUserByNameWithNullThrowsException() {
        userService.loadUserByUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadUserByNameWithEmptyThrowsException() {
        userService.loadUserByUsername("");
    }

    @Test
    public void queryUsersWorks() {
        when(userRepository.findAllByUsernameContainingIgnoreCase(user.getUsername()))
                .thenReturn(Collections.singletonList(user));

        assertThat(1, is(equalTo(userService.queryUsers(user.getUsername()).size())));
    }

    @Test
    public void queryUsersWithNoResults() {
        when(userRepository.findAllByUsernameContainingIgnoreCase(user.getUsername())).thenReturn(Collections.emptyList());

        assertTrue(userService.queryUsers(user.getUsername()).isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void queryUsersWithNullThrowsException() {
        userService.queryUsers(null);
    }

    @Test
    public void updateAllWorks() {
        Reflection.setField(user, "id", 1L);

        when(userRepository.findOneByUsernameAndEnabledTrue(userDto.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findOne(1L)).thenReturn(user);

        userService.updateAll(1, userDto);
    }

    @Test(expected = NullPointerException.class)
    public void updateAllWithDtoNullThrowsException() {
        userService.updateAll(1, null);
    }

    @Test(expected = ServiceException.class)
    public void updateAllWithMissingUserThrowsException() {
        when(userRepository.findOne(1L)).thenReturn(null);

        userService.updateAll(1, userDto);
    }

    @Test(expected = ServiceException.class)
    public void updateAllSettingNameToExistingUserThrowsException() {
        final User existingUser = new User("existing", "password");
        Reflection.setField(existingUser, "id", 1L);

        when(userRepository.findOneByUsernameAndEnabledTrue("existing")).thenReturn(Optional.of(existingUser));
        when(userRepository.findOne(2L)).thenReturn(user);

        userDto.setUsername(existingUser.getUsername());

        userService.updateAll(2, userDto);
    }

    @Test
    public void updateProfileWithCurrentUserWorks() {
        when(userRepository.findOneByUsernameAndEnabledTrue(user.getUsername())).thenReturn(Optional.of(user));
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.of(user.getUsername()));

        userService.updateProfile(userDto);

        verify(userRepository).save(user);
    }

    @Test(expected = ServiceException.class)
    public void updateProfileWithNoCurrentUserThrowsException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn(Optional.empty());

        userService.updateProfile(userDto);
    }

    @Test(expected = NullPointerException.class)
    public void updateProfileWithDtoNullThrowsException() {
        userService.updateProfile(null);
    }
}