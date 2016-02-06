package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.Permission;
import org.outofrange.crowdsupport.model.Role;
import org.outofrange.crowdsupport.persistence.PermissionRepository;
import org.outofrange.crowdsupport.persistence.RoleRepository;
import org.outofrange.crowdsupport.util.ServiceException;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
        roles.add(new Role(roles.get(0).getName()));
        when(roleRepositoryMock.findAll()).thenReturn(roles);

        final Set<Role> result = authorityService.loadAllRoles();

        assertThat(someRoles.size(), is(equalTo(result.size())));
        assertTrue(someRoles.containsAll(result));
    }

    @Test
    public void loadAllPermissionsReturnsMultiplePermissions() {
        when(permissionRepositoryMock.findAll()).thenReturn(somePermissions);

        final Set<Permission> result = authorityService.loadAllPermissions();

        assertThat(somePermissions.size(), is(equalTo(result.size())));
        assertTrue(somePermissions.containsAll(result));
    }

    @Test
    public void loadAllPermissionsReturnsNoDuplicatePermissions() {
        final List<Permission> permissions = new ArrayList<>(somePermissions);
        permissions.add(new Permission(permissions.get(0).getName()));
        when(permissionRepositoryMock.findAll()).thenReturn(permissions);

        final Set<Permission> result = authorityService.loadAllPermissions();

        assertThat(somePermissions.size(), is(equalTo(result.size())));
        assertTrue(somePermissions.containsAll(result));
    }

    @Test
    public void settingDuplicatePermissionsWontSetDuplicatePermissions() {
        final Role r = new Role("ROLE_NAME");
        final Permission p1 = new Permission("P1");
        final Permission p2 = new Permission("P2");
        final Permission p3 = new Permission("P3");

        when(roleRepositoryMock.findOneByName(r.getName())).thenReturn(Optional.of(r));
        when(permissionRepositoryMock.findOneByName(p1.getName())).thenReturn(Optional.of(p1));
        when(permissionRepositoryMock.findOneByName(p2.getName())).thenReturn(Optional.of(p2));
        when(permissionRepositoryMock.findOneByName(p3.getName())).thenReturn(Optional.of(p3));

        authorityService.setPermissionsForRole(r.getName(), Arrays.asList(p1.getName(), p2.getName(), p3.getName(),
                p3.getName()));

        final Set<Permission> loadedPermissions = authorityService.loadRole(r.getName()).getPermissions();

        assertThat(3, is(equalTo(loadedPermissions.size())));
        assertTrue(loadedPermissions.contains(p1));
        assertTrue(loadedPermissions.contains(p2));
        assertTrue(loadedPermissions.contains(p3));

    }

    @Test(expected = ServiceException.class)
    public void addingUnknownPermissionToKnownRoleThrowsException() {
        final Role r = new Role("ROLE_NAME");

        when(roleRepositoryMock.findOneByName(r.getName())).thenReturn(Optional.of(r));
        when(permissionRepositoryMock.findOneByName(p1.getName())).thenReturn(Optional.empty());

        authorityService.setPermissionsForRole(r.getName(), Collections.singletonList(p1.getName()));
    }

    @Test(expected = ServiceException.class)
    public void addingKnownPermissionToUnknownRoleThrowsException() {
        final Role r = new Role("ROLE_NAME");

        when(roleRepositoryMock.findOneByName(r.getName())).thenReturn(Optional.empty());
        when(permissionRepositoryMock.findOneByName(p1.getName())).thenReturn(Optional.of(p1));

        authorityService.setPermissionsForRole(r.getName(), Collections.singletonList(p1.getName()));
    }

    @Test(expected = ServiceException.class)
    public void deletionOfSystemRoleThrowsError() {
        final Role r = new Role("ROLE_R");
        r.setSystemRole(true);
        when(roleRepositoryMock.findOneByName(r.getName())).thenReturn(Optional.of(r));

        authorityService.deleteRole(r.getName());
    }

    @Test
    public void deletionOfNonSystemRoleWorks() {
        when(roleRepositoryMock.findOneByName(r1.getName())).thenReturn(Optional.of(r1));

        authorityService.deleteRole(r1.getName());

        verify(roleRepositoryMock).delete(r1);
    }

    @Test
    public void createRoleIfNeededWhenNoRole() {
        when(roleRepositoryMock.findOneByName(r1.getName())).thenReturn(Optional.empty());

        authorityService.createRoleIfNeeded(r1.getName());

        verify(roleRepositoryMock).save(new Role(r1.getName()));
    }

    @Test
    public void createRoleIfNeededWhenRoleExisting() {
        when(roleRepositoryMock.findOneByName(r1.getName())).thenReturn(Optional.of(r1));

        authorityService.createRoleIfNeeded(r1.getName());

        verify(roleRepositoryMock, never()).save(new Role(r1.getName()));
    }
}