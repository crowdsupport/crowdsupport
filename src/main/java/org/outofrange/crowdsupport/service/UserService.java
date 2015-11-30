package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if ("".equals(user.getEmail())) {
            user.setEmail(null);
        }

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username).get();
    }
}
