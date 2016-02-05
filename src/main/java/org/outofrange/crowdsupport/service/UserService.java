package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User updateProfile(FullUserDto userDto);

    User createUser(FullUserDto userDto);

    User updateAll(long userId, FullUserDto userDto);

    List<User> queryUsers(String like);

    User loadUser(long id);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    Optional<User> getCurrentUser();

    Optional<User> getCurrentUserUpdated();

    List<User> loadAll();
}
