package org.outofrange.crowdsupport.service.impl;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.service.UserService;
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
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private ModelMapper mapper;

    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        if (user.rehashPassword()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username).get();
    }

    @Override
    public Optional<User> getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return Optional.of((User) principal);
        } else {
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
    public UserDto getCurrentUserDto() {
        final Optional<User> user = getCurrentUser();

        if (user.isPresent()) {
            return mapper.map(user.get(), UserDto.class);
        } else {
            return null;
        }
    }
}
