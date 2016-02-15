package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface AuthorityService {
    /**
     * Sets the permissions associated with a role.
     * <p>
     * Duplicate permissions will be removed.
     *
     * @param role        the name of the role to look for
     * @param permissions the permissions to set
     * @return the updated role
     * @throws ServiceException if either the role or a permission couldn't be found
     */
    Role setPermissionsForRole(String role, Collection<String> permissions);

    /**
     * Creates a new role if it doesn't exist already.
     *
     * @param roleName the name of the role
     * @return the persisted role
     */
    Role createRoleIfNeeded(String roleName);

    /**
     * Deletes a role as long as it isn't a system role.
     * <p>
     * Won't do anything if the role couldn't be found.
     *
     * @param roleName the name of the role
     * @throws ServiceException if the found role is a system role
     */
    void deleteRole(String roleName);

    /**
     * Loads all persisted roles.
     *
     * @return all persisted roles.
     */
    Set<Role> loadAllRoles();

    /**
     * Loads a role.
     *
     * @param name the name of the role
     * @return the loaded role, or null if none could be found
     */
    Role loadRole(String name);

    /**
     * Loads all permissions.
     *
     * @return all permissions
     */
    Set<Permission> loadAllPermissions();

    /**
     * Creates a set of {@link GrantedAuthority}s for a collection of role names.
     * <p>
     * For each role, the role itself and all of its permissions are added to the set.
     * <p>
     * This method uses a cache to avoid querying the database more than necessary.
     *
     * @param roleNames the names of the roles to map
     * @return the mapped authorities
     */
    Set<GrantedAuthority> mapRolesToAuthorities(Collection<String> roleNames);
}
