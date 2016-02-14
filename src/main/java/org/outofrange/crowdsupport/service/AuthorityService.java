package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface AuthorityService {
    Role setPermissionsForRole(String role, Collection<String> permissions);

    Role createRoleIfNeeded(String name);

    void deleteRole(String roleName);

    Set<Role> loadAllRoles();

    Role loadRole(String name);

    Set<Permission> loadAllPermissions();

    Set<GrantedAuthority> mapRolesToAuthorities(Collection<String> roleNames);
}
