package org.outofrange.crowdsupport.util;

import org.springframework.stereotype.Component;

/**
 * Stores strings representations of all {@link org.outofrange.crowdsupport.model.Permission}s and exposing them as
 * a bean.
 */
@Component("perm")
public class PermissionStore {
    /**
     * Allows to see open place requests.
     */
    public static final String PROCESS_PLACE_REQUESTS = "PROCESS_PLACE_REQUESTS";

    /**
     * Allows a user to accept or decline new place requests.
     */
    public static final String MANAGE_PLACES = "MANAGE_PLACES";

    /**
     * Allows a user to create new roles, delete already created (non system) roles, and assign new permissions to all
     * of them.
     */
    public static final String MANAGE_ROLES = "MANAGE_ROLES";

    /**
     * Allows a user to edit user details of all other users, and to assign them new roles.
     */
    public static final String MANAGE_USERS = "MANAGE_USERS";

    /**
     * Allows a user to create new cities.
     */
    public static final String MANAGE_CITIES = "MANAGE_CITIES";

    /**
     * Allows a user to create new states.
     */
    public static final String MANAGE_STATES = "MANAGE_STATES";

    /**
     * Allows a user to query information about all users at once.
     */
    public static final String QUERY_USERS = "QUERY_USERS";

    /**
     * Allows a user to change application settings.
     */
    public static final String CONFIGURE_APPLICATION = "CONFIGURE_APPLICATION";
}
