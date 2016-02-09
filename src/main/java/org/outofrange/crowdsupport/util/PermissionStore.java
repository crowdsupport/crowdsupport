package org.outofrange.crowdsupport.util;

import org.springframework.stereotype.Component;

@Component("perm")
public class PermissionStore {
    public static final String PROCESS_PLACE_REQUESTS = "PROCESS_PLACE_REQUESTS";
    public static final String MANAGE_ROLES = "MANAGE_ROLES";
    public static final String MANAGE_USERS = "MANAGE_USERS";
    public static final String MANAGE_CITIES = "MANAGE_CITIES";
    public static final String MANAGE_STATES = "MANAGE_STATES";
    public static final String MANAGE_PLACES = "MANAGE_PLACES";
    public static final String QUERY_USERS = "QUERY_USERS";
    public static final String CONFIGURE_APPLICATION = "CONFIGURE_APPLICATION";
}
