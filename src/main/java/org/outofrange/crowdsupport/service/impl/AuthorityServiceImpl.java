package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private static final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private PermissionRepository permissionRepository;

    @Override
    public Role setPermissionsForRole(String role, Collection<String> permissions) {
        log.debug("Setting permissions for role {}: {}", role, permissions);

        final Role roleDb = roleRepository.findOneByName(role).get();

        roleDb.setPermissions(permissions.stream()
                .map(p -> permissionRepository.findOneByName(p).get()).collect(Collectors.toSet()));

        return roleRepository.save(roleDb);
    }

    @Override
    public User setRolesForUser(User user, Collection<Role> roles) {
        log.debug("Setting roles for user {}: {}", user, roles);

        final User userDb = userRepository.findOne(user.getId());

        userDb.setRoles(roles);

        return userRepository.save(userDb);
    }

    @Override
    public Role createRole(String roleName) {
        log.debug("Creating role {}", roleName);

        if (roleRepository.findOneByName(roleName).isPresent()) {
            throw new ServiceException("Already found a role called " + roleName);
        }

        return roleRepository.save(new Role(roleName));
    }

    @Override
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
    public Set<Permission> loadAllPermissions() {
        log.debug("Querying all permissions");

        return new HashSet<>(permissionRepository.findAll());
    }
}
