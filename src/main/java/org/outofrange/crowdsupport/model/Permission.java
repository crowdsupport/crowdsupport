package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Sanitizer;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A permission is used to control access to certain functionality, depending on the current authentication.
 *
 * @see Role
 */
@Entity
@Table(name = "PERMISSIONS")
public class Permission extends BaseEntity implements GrantedAuthority {
    /**
     * The name of the permission.
     */
    @Column(name = "name")
    private String name;

    protected Permission() { /* empty constructor for frameworks */ }

    /**
     * Creates a new permission.
     *
     * @param name the name of the newly created permission
     * @see #setName(String)
     */
    public Permission(String name) {
        setName(name);
    }

    /**
     * Returns the name of the permission.
     *
     * @return the name of the permission
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the permission.
     * <p>
     * The {@code name} will automatically be converted to uppercase characters.
     *
     * @param name the name of the permission.
     * @throws NullPointerException     when {@code name} is null
     * @throws IllegalArgumentException when {@code name} is empty or contains other characters than letters, numbers
     *                                  and underscores
     */
    public void setName(String name) {
        this.name = Sanitizer.sanitizeAuthorityName(name);
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    /**
     * A permission is equal to another permission if they have the same {@code name}.
     *
     * @param o another permission to check equality with
     * @return true if {@code o} is another permission with the same {@code name}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    /**
     * Returns a hashcode for the permission.
     *
     * @return a hashcode for the permissions
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
}
