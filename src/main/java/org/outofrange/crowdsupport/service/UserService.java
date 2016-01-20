package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.dto.CurrentUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User>, UserDetailsService {
    User updateProfile(CurrentUserDto userDto);

    User updateAll(String username, CurrentUserDto userDto);

    List<User> queryUsers(String like);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    Optional<User> getCurrentUser();

    Optional<User> getCurrentUserUpdated();

    boolean isUserLoggedIn();
}
