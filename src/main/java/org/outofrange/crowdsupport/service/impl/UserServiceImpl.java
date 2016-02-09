package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.RoleStore;
import org.outofrange.crowdsupport.util.ServiceException;
import org.outofrange.crowdsupport.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // it's easier to test this way...
    private CurrentUserProvider currentUserProvider = new CurrentUserProvider();

    @Inject
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    protected void setCurrentUserProvider(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole(@role.USER)")
    public User updateProfile(FullUserDto userDto) {
        final Optional<User> optionalUser = getCurrentUserUpdated();

        if (!optionalUser.isPresent()) {
            throw new ServiceException("No current user to update");
        }

        final User self = optionalUser.get();

        if (userDto.getUsername() != null && !self.getUsername().equals(userDto.getUsername())) {
            throw new ServiceException("Updating different user is not allowed!");
        }

        return updateUser(self, userDto, false);
    }

    @Override
    @Transactional(readOnly = false)
    public User createUser(FullUserDto userDto) {
        log.debug("Creating user: {}", userDto);

        User user = new User(userDto.getUsername(), userDto.getPassword());
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setImagePath(userDto.getImagePath());
        user.setEnabled(true);

        if (userRepository.findOneByUsername(userDto.getUsername()).isPresent()) {
            throw new ServiceException("Found already existing user with username " + userDto.getUsername());
        }

        user.getRoles().add(roleRepository.findOneByName(RoleStore.USER).get());

        user = userRepository.save(user);
        Events.user(ChangeType.CREATE, user).publish();

        return user;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasAuthority(@perm.QUERY_USERS)")
    public User updateAll(long userId, FullUserDto userDto) {
        Validate.notNull(userDto);

        final User user = userRepository.findOne(userId);

        if (user == null) {
            throw new ServiceException("Couldn't find user with id " + userId);
        }

        final Optional<User> existingUser = userRepository.findOneByUsername(userDto.getUsername());
        if (userId != existingUser.get().getId()) {
            throw new ServiceException("Can't set username (already used!");
        }

        return updateUser(user, userDto, true);
    }

    private User updateUser(User user, FullUserDto userDto, boolean all) {
        Validate.notNull(user);
        Validate.notNull(userDto);

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getImagePath() != null) {
            user.setImagePath(userDto.getImagePath());
        }

        if (userDto.getPassword() != null && !"".equals(userDto.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        }

        if (all) {
            if (userDto.getImagePath() != null) {
                user.setUsername(userDto.getUsername());
            }
            if (userDto.getRoles() != null) {
                user.setRoles(userDto.getRoles().stream().map(r -> roleRepository.findOneByName(r).get()).collect(Collectors.toSet()));
            }
        }

        final User updatedUser = userRepository.save(user);
        Events.user(ChangeType.UPDATE, updatedUser).publish();
        return updatedUser;
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.QUERY_USERS)")
    public List<User> loadAll() {
        log.debug("Loading all users");

        return userRepository.findAll();
    }
    
    @Override
    @PreAuthorize("hasAuthority(@perm.QUERY_USERS)")
    public List<User> queryUsers(String like) {
        log.debug("Searching users for {}", like);

        Validate.notNull(like);
        
        return userRepository.findAllByUsernameContainingIgnoreCase(like);
    }

    @Override
    public User loadUser(long id) {
        log.debug("Loading user with id {}", id);

        return userRepository.findOne(id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        Validate.notNullOrEmpty(username);

        final Optional<User> user = userRepository.findOneByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Couldn't find user with username " + username);
        }
    }

    @Override
    public Optional<User> getCurrentUser() {
        log.debug("Reading current user from security context...");

        return currentUserProvider.getCurrentUser();
    }

    @Override
    public Optional<User> getCurrentUserUpdated() {
        final Optional<User> currentUser = getCurrentUser();

        if (currentUser.isPresent()) {
            final Optional<User> loadedUser = userRepository.findOneByUsername(currentUser.get().getUsername());

            if (!loadedUser.isPresent()) {
                throw new ServiceException("Can't find user anymore: " + currentUser.get().getUsername());
            }

            return loadedUser;
        } else {
            return currentUser;
        }
    }
}
