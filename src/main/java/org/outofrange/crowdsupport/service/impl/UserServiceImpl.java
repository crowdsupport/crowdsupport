package org.outofrange.crowdsupport.service.impl;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.spring.security.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private ModelMapper mapper;

    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        log.trace("Saving user {}", user);

        if (user.rehashPassword()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> loadAll() {
        log.trace("Loading all users");

        return userRepository.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("Loading user by username: {}", username);

        return userRepository.findOneByUsername(username).get();
    }

    @Override
    public Optional<User> getCurrentUser() {
        log.trace("Reading current user from security context...");

        final Object userAuth = SecurityContextHolder.getContext().getAuthentication();

        if (userAuth instanceof UserAuthentication) {
            final UserAuthentication u = (UserAuthentication) userAuth;
            log.trace("Found user: {}", u.getDetails());
            return Optional.of(u.getDetails());
        } else {
            log.trace("Found no user");
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

    @Override
    public boolean isUserLoggedIn() {
        return getCurrentUser().isPresent();
    }
}
