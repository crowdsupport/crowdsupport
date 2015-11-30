package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends CrowdsupportService<User>, UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
