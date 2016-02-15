package org.outofrange.crowdsupport.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthorityServiceImpl implements AuthorityService {
    private static final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final Multimap<String, GrantedAuthority> roleMappingCache = HashMultimap.create();

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Inject
    public AuthorityServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_ROLES)")
    @Transactional(readOnly = false)
    public Role setPermissionsForRole(String role, Collection<String> permissions) {
        log.debug("Setting permissions for role {}: {}", role, permissions);

        final Optional<Role> optionalRole = roleRepository.findOneByName(role);
        if (!optionalRole.isPresent()) {
            throw new ServiceException("Couldn't find role with name " + role);
        }

        final Role roleDb = optionalRole.get();

        roleDb.setPermissions(permissions.stream()
                .map(p -> {
                    final Optional<Permission> optionalPermission = permissionRepository.findOneByName(p);
                    if (!optionalPermission.isPresent()) {
                        throw new ServiceException("Couldn't find permission with name " + p);
                    }
                    return optionalPermission.get();
                }).collect(Collectors.toSet()));

        invalidateRoleMappingCache(role);
        return roleRepository.save(roleDb);
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_ROLES)")
    @Transactional(readOnly = false)
    public Role createRoleIfNeeded(String roleName) {
        log.debug("Creating role {}", roleName);

        final Optional<Role> roleDb = roleRepository.findOneByName(roleName);
        if (!roleDb.isPresent()) {
            return roleRepository.save(new Role(roleName));
        } else {
            return roleDb.get();
        }
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_ROLES)")
    @Transactional(readOnly = false)
    public void deleteRole(String roleName) {
        log.debug("Deleting role {}", roleName);

        final Role role = roleRepository.findOneByName(roleName).get();
        if (role != null) {
            if (role.isSystemRole()) {
                throw new ServiceException("Can't delete system roles");
            }

            roleRepository.delete(role);
            invalidateRoleMappingCache(roleName);
        }
    }

    @Override
    public Set<Role> loadAllRoles() {
        log.debug("Querying all roles");

        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    public Role loadRole(String name) {
        log.debug("Loading role with name {}", name);

        return roleRepository.findOneByName(name).get();
    }

    @Override
    public Set<Permission> loadAllPermissions() {
        log.debug("Querying all permissions");

        return new HashSet<>(permissionRepository.findAll());
    }

    @Override
    public Set<GrantedAuthority> mapRolesToAuthorities(Collection<String> roleNames) {
        if (roleNames == null) {
            return Collections.emptySet();
        }

        final Set<GrantedAuthority> authorities = new HashSet<>();
        roleNames.forEach(name -> authorities.addAll(mapRoleToAuthorities(name)));

        return authorities;
    }

    /**
     * Invalidates the roleMapping cache of a specific role
     * @param roleName the name of the role to invalidate the cache for
     */
    private void invalidateRoleMappingCache(String roleName) {
        roleMappingCache.removeAll(roleName);
    }

    /**
     * Creates a set of {@link GrantedAuthority}s for a role.
     * <p>
     * The role itself and all of its permissions are added to the set.
     * <p>
     * This method uses a cache to avoid querying the database more than necessary.
     *
     * @param roleName the name of the role to map
     * @return the mapped authorities
     */
    private Set<GrantedAuthority> mapRoleToAuthorities(String roleName) {
        log.trace("Mapping role {} to authorities", roleName);

        if (roleMappingCache.containsKey(roleName)) {
            return new HashSet<>(roleMappingCache.get(roleName));
        } else {
            final Optional<Role> loadedRole = roleRepository.findOneByName(roleName);
            if (!loadedRole.isPresent()) {
                log.warn("Tried to map non existent role {} - returning empty authorities");
                return Collections.emptySet();
            }

            final Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(loadedRole.get());
            authorities.addAll(loadedRole.get().getPermissions());

            roleMappingCache.putAll(roleName, authorities);
            return authorities;
        }
    }
}
