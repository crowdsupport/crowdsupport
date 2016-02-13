package org.outofrange.crowdsupport.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public class AuthorityResolver {
    private static final Multimap<String, GrantedAuthority> rolePermissionMap = HashMultimap.create();

    static {
        rolePermissionMap.putAll(RoleStore.ADMIN, Arrays.asList(
                new SimpleGrantedAuthority(RoleStore.ADMIN),
                new SimpleGrantedAuthority(PermissionStore.CONFIGURE_APPLICATION),
                new SimpleGrantedAuthority(PermissionStore.MANAGE_CITIES),
                new SimpleGrantedAuthority(PermissionStore.MANAGE_PLACES),
                new SimpleGrantedAuthority(PermissionStore.MANAGE_ROLES),
                new SimpleGrantedAuthority(PermissionStore.MANAGE_STATES),
                new SimpleGrantedAuthority(PermissionStore.MANAGE_USERS),
                new SimpleGrantedAuthority(PermissionStore.PROCESS_PLACE_REQUESTS),
                new SimpleGrantedAuthority(PermissionStore.QUERY_USERS)));

        rolePermissionMap.put(RoleStore.USER, new SimpleGrantedAuthority(RoleStore.USER));
    }

    private AuthorityResolver() { /* no instantiation */ }

    public static Collection<GrantedAuthority> resolveRoles(String roleName) {
        return rolePermissionMap.get(roleName);
    }
}
