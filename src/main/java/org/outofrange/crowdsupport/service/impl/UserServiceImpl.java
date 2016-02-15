package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.Role;
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

    private final MailSenderImpl mailSender;

    // it's easier to test this way...
    private CurrentUserProvider currentUserProvider = new CurrentUserProvider();

    @Inject
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           MailSenderImpl mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

        this.mailSender = mailSender;
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
        boolean updateAll = false;

        if (userDto.getUsername() != null && !self.getUsername().equals(userDto.getUsername())) {
            if (!self.isAdmin()) {
                throw new ServiceException("Updating different user is not allowed!");
            } else {
                updateAll = true;
            }
        }

        return updateUser(self, userDto, updateAll);
    }

    @Override
    @Transactional(readOnly = false)
    public void disableUser(long userId) {
        log.debug("Disabling user with id {}", userId);

        final User user = userRepository.findOne(userId);

        if (user != null) {
            if (user.isEnabled()) {
                user.setEnabled(false);

                userRepository.save(user);
            } else {
                log.debug("User is disabled already, doing nothing");
            }
        } else {
            throw new ServiceException("Found no user with id " + userId);
        }
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasAuthority(@role.ADMIN)")
    public User makeAdmin(long userId) {
        final User user = userRepository.findOne(userId);

        if (user != null) {
            final Role adminRole = roleRepository.findOneByName(RoleStore.ADMIN).get();

            if (!user.getRoles().contains(adminRole)) {
                user.getRoles().add(adminRole);

                userRepository.save(user);
            }

            return user;
        } else {
            throw new ServiceException("Couldn't find user with id " + userId);
        }
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

        if (userRepository.findOneByUsernameAndEnabledTrue(userDto.getUsername()).isPresent()) {
            throw new ServiceException("Found already existing, enabled user with username " + userDto.getUsername());
        }

        user.getRoles().add(roleRepository.findOneByName(RoleStore.USER).get());

        user = userRepository.save(user);
        Events.user(ChangeType.CREATE, user).publish();

        if (user.hasEmailToBeConfirmed()) {
            sendConfirmationEmail(user);
        }

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

        final Optional<User> existingUser = userRepository.findOneByUsernameAndEnabledTrue(userDto.getUsername());
        if (userId != existingUser.get().getId()) {
            throw new ServiceException("Can't set username (already used!)");
        }

        return updateUser(user, userDto, true);
    }

    private User updateUser(User user, FullUserDto userDto, boolean all) {
        Validate.notNull(user);
        Validate.notNull(userDto);

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
            sendConfirmationEmail(user);
        }

        if (userDto.getImagePath() != null) {
            user.setImagePath(userDto.getImagePath());
        }

        if (userDto.getPassword() != null && !"".equals(userDto.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        }

        if (all) {
            if (userDto.getUsername() != null) {
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

        final Optional<User> user = userRepository.findOneByUsernameAndEnabledTrue(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Couldn't find user with username " + username);
        }
    }

    @Override
    public Optional<String> getCurrentUsername() {
        log.debug("Reading current username from security context...");

        return currentUserProvider.getCurrentUsername();
    }

    @Override
    public Optional<User> getCurrentUserUpdated() {
        final Optional<String> currentUsername = getCurrentUsername();

        if (currentUsername.isPresent()) {
            final Optional<User> loadedUser = userRepository.findOneByUsernameAndEnabledTrue(currentUsername.get());

            if (!loadedUser.isPresent()) {
                throw new ServiceException("Can't find user anymore: " + currentUsername.get());
            }

            return loadedUser;
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void confirmEmail(String emailConfirmationId) {
        final Optional<User> user = userRepository.findOneByEmailConfirmationId(emailConfirmationId);

        if (user.isPresent()) {
            final User u = user.get();
            log.debug("Found user {} with email {} for confirmation id {} - confirming", u.getUsername(), u.getEmail(),
                    emailConfirmationId);
            u.setEmailConfirmationIdNull();
            userRepository.save(u);
        } else {
            throw new ServiceException("Couldn't find a user to confirm with id " + emailConfirmationId);
        }
    }

    @Override
    public void sendConfirmationEmail(User user) {
        log.info("Sending confirmation email to {}", user.getEmail());

        String text = "<html><body>Hi! You've used your email address on crowdsupport. Please follow this link to " +
                "confirm your address!<br/><br/><a href=\"http://localhost/profile/confirmMail/" +
                user.getEmailConfirmationId() + "\">Crowdsupport</a></body></html>";

        mailSender.sendMessage(user.getEmail(), "Confirm your email address", text);
    }
}
