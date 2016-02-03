package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.spring.security.UserAuthentication;
import org.outofrange.crowdsupport.util.RoleStore;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private AuthorityService authorityService;

    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        log.debug("Saving user {}", user);

        if (user.rehashPassword()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole(@role.USER)")
    public User updateProfile(FullUserDto userDto) {
        final User self = getCurrentUserUpdated().get();

        if (userDto.getUsername() != null && !self.getUsername().equals(userDto.getUsername())) {
            throw new ServiceException("Updating different user is not allowed!");
        }

        return updateUser(self, userDto, false);
    }

    @Override
    public User createUser(FullUserDto userDto) {
        log.debug("Creating user: {}", userDto);

        final User user = new User(userDto.getUsername(), userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setImagePath(userDto.getImagePath());
        user.setEnabled(true);
        user.getRoles().add(authorityService.loadRole(RoleStore.USER));

        Events.userChanged(ChangeType.ADD, user).publish();

        return save(user);
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasAuthority(@perm.QUERY_USERS)")
    public User updateAll(long userId, FullUserDto userDto) {
        final User user = userRepository.findOne(userId);

        return updateUser(user, userDto, true);
    }

    private User updateUser(User user, FullUserDto userDto, boolean all) {
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getImagePath() != null) {
            user.setImagePath(userDto.getImagePath());
        }

        if (userDto.getPassword() != null && !"".equals(userDto.getPassword())) {
            user.setPassword(userDto.getPassword());
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
        Events.userChanged(ChangeType.REFRESH, updatedUser).publish();
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

        return userRepository.findOneByUsername(username).get();
    }

    @Override
    public Optional<User> getCurrentUser() {
        log.debug("Reading current user from security context...");

        final Object userAuth = SecurityContextHolder.getContext().getAuthentication();

        if (userAuth instanceof UserAuthentication) {
            final UserAuthentication u = (UserAuthentication) userAuth;
            log.debug("Found user: {}", u.getDetails());
            return Optional.of(u.getDetails());
        } else {
            log.debug("Found no user");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getCurrentUserUpdated() {
        final Optional<User> user = getCurrentUser();

        if (user.isPresent()) {
            return Optional.of(loadUserByUsername(user.get().getUsername()));
        } else {
            return user;
        }
    }
}
