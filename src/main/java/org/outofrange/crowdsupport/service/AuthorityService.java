package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.model.User;

import java.util.Collection;
import java.util.Set;

public interface AuthorityService {
    Role setPermissionsForRole(Role role, Collection<Permission> permission);

    User setRolesForUser(User user, Collection<Role> role);

    Role createRole(Role role);

    void deleteRole(Role role);

    Set<Role> loadAllRoles();

    Set<Permission> loadAllPermissions();
}
