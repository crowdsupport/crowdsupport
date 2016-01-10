package org.outofrange.crowdsupport.service.impl;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.rest.dto.UserDto;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private ModelMapper mapper;

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if ("".equals(user.getEmail())) {
                user.setEmail(null);
            }
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
    public UserDto getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return mapper.map(principal, UserDto.class);
        } else {
            return null;
        }
    }
}
