package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
public class Role extends BaseEntity implements GrantedAuthority {
    private static final String ROLE_PREFIX = "ROLE_";

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "RolePermissions", joinColumns = {@JoinColumn(name = "role")},
            inverseJoinColumns = {@JoinColumn(name = "permission")})
    private Set<Permission> permissions = new HashSet<>();

    @Column(name = "systemrole")
    private boolean systemRole;

    protected Role() {
        // framework constructor
    }

    public Role(String name, Permission... permissions) {
        setName(name);

        if (permissions != null) {
            this.permissions = new HashSet<>(Arrays.asList(permissions));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase().replaceAll("\\s+", "_");
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        final Set<Permission> permissionSet;
        if (permissions instanceof Set) {
            permissionSet = (Set<Permission>) permissions;
        } else {
            permissionSet = new HashSet<>(permissions);
        }

        this.permissions = permissionSet;
    }

    @Override
    public String getAuthority() {
        return ROLE_PREFIX + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return new EqualsBuilder()
                .append(name, role.name)
                .append(permissions, role.permissions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(permissions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    public boolean isSystemRole() {
        return systemRole;
    }

    public Role setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
        return this;
    }
}
