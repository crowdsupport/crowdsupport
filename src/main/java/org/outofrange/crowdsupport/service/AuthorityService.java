package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.model.User;

import java.util.Collection;
import java.util.Set;

public interface AuthorityService {
    Role setPermissionsForRole(String role, Collection<String> permissions);

    User setRolesForUser(User user, Collection<Role> role);

    Role createRoleIfNeeded(String name);

    Role addPermissionToRole(String roleName, String permission);

    Role removePermissionFromRole(String roleName, String permission);

    void deleteRole(String roleName);

    Set<Role> loadAllRoles();

    Role loadRole(String name);

    Set<Permission> loadAllPermissions();
}
