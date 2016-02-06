package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorityServiceImplTest {
    private RoleRepository roleRepositoryMock;
    private PermissionRepository permissionRepositoryMock;

    private AuthorityServiceImpl authorityService;

    private final List<Role> someRoles = new ArrayList<>();
    private final List<Permission> somePermissions = new ArrayList<>();

    private final Permission p1 = new Permission("p1");
    private final Permission p2 = new Permission("p2");

    private final Role r1 = new Role("ROLE_1", p1, p2);
    private final Role r2 = new Role("ROLE_2", p1);

    @Before
    public void prepare() {
        someRoles.addAll(Arrays.asList(r1, r2));
        somePermissions.addAll(Arrays.asList(p1, p2));

        roleRepositoryMock = mock(RoleRepository.class);
        permissionRepositoryMock = mock(PermissionRepository.class);
        authorityService = new AuthorityServiceImpl(roleRepositoryMock, permissionRepositoryMock);
    }

    @Test
    public void loadAllRolesReturnsMultipleRoles() {
        when(roleRepositoryMock.findAll()).thenReturn(someRoles);

        final Set<Role> result = authorityService.loadAllRoles();

        assertThat(someRoles.size(), is(equalTo(result.size())));
        assertTrue(someRoles.containsAll(result));
    }

    @Test
    public void loadAllRolesReturnsNoDuplicateRoles() {
        final List<Role> roles = new ArrayList<>(someRoles);
        roles.add(new Role("ROLE_1"));
        when(roleRepositoryMock.findAll()).thenReturn(roles);

        final Set<Role> result = authorityService.loadAllRoles();

        assertThat(someRoles.size(), is(equalTo(result.size())));
        assertTrue(someRoles.containsAll(result));
    }
}