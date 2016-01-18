package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends BaseService<User>, UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    Optional<User> getCurrentUser();

    Optional<User> getCurrentUserUpdated();

    boolean isUserLoggedIn();
}
