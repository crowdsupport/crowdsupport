package org.outofrange.crowdsupport.util;

import org.springframework.stereotype.Component;

@Component("role")
public class RoleStore {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
}
