package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthorityServiceImpl implements AuthorityService {
    private static final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

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

        final Role roleDb =  optionalRole.get();

        roleDb.setPermissions(permissions.stream()
                .map(p -> {
                    final Optional<Permission> optionalPermission = permissionRepository.findOneByName(p);
                    if (!optionalPermission.isPresent()) {
                        throw new ServiceException("Couldn't find permission with name " + p);
                    }
                    return optionalPermission.get();
                }).collect(Collectors.toSet()));

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
        if (role.isSystemRole()) {
            throw new ServiceException("Can't delete system roles");
        }

        roleRepository.delete(role);
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
}
