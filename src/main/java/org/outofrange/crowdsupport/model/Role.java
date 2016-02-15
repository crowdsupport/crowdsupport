package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Sanitizer;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A role describes a specific set of permissions assignable to users.
 *
 * @see Permission
 */
@Entity
@Table(name = "ROLES")
public class Role extends BaseEntity implements GrantedAuthority {
    /**
     * The string every role has to be prefixed with.
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * The name of the role.
     */
    @Column(name = "name")
    private String name;

    /**
     * A set of permissions assigned to this role.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROLES_PERMISSIONS", joinColumns = {@JoinColumn(name = "role")},
            inverseJoinColumns = {@JoinColumn(name = "permission")})
    private Set<Permission> permissions = new HashSet<>();

    /**
     * A flag indicating if this is a system role. System roles can't be deleted.
     */
    @Column(name = "system_role")
    private boolean systemRole;

    protected Role() { /* empty constructor for frameworks */ }

    /**
     * Creates a new role
     *
     * @param name        the name of the role
     * @param permissions the permissions to associate with the role
     * @see #setName(String)
     */
    public Role(String name, Permission... permissions) {
        setName(name);

        if (permissions != null) {
            this.permissions.addAll(Arrays.asList(permissions));
        }
    }

    /**
     * Returns the name of the role.
     *
     * @return the name of the role
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     * <p>
     * This will automatically add {@link #ROLE_PREFIX} if it's not there already and uppercase all letters.
     *
     * @param name the name of the role, will be uppercased and prefixed if necessary
     * @throws NullPointerException     if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty or contains other charachters than letters, numbers
     *                                  and underscores
     */
    public void setName(String name) {
        this.name = Sanitizer.sanitizeAuthorityName(name, ROLE_PREFIX);
    }

    /**
     * Returns the set of permissions for this role.
     *
     * @return the set of permissions for this role
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions for this role.
     * <p>
     * Duplicate permissions will be removed.
     *
     * @param permissions the permissions to set for this role
     */
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
        return getName();
    }

    /**
     * A role is equal to another role if they share the same name.
     *
     * @param o the other object to check equality with
     * @return true if Role {@code o} has the same name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return new EqualsBuilder()
                .append(name, role.name)
                .isEquals();
    }

    /**
     * Returns a hash code for the role.
     *
     * @return a hash code for the role
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    /**
     * Returns if the role is a system role.
     * <p>
     * System roles can't be deleted, as they are defined by the application, not by users.
     *
     * @return if the role is a system role
     */
    public boolean isSystemRole() {
        return systemRole;
    }

    /**
     * Sets if the role is a system role.
     *
     * @param systemRole the value indicating if the role is a system role
     * @return this
     */
    public Role setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
        return this;
    }
}
