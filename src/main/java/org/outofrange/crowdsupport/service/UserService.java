package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class UserService {
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

    public Optional<User> loadUser(String username, String password) {
        final Optional<User> loadedUser = userRepository.findOneByUsername(username);

        if (loadedUser.isPresent() && passwordEncoder.matches(password, loadedUser.get().getPassword())) {
            return loadedUser;
        }

        return Optional.empty();
    }
}
