package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Permissions")
public class Permission extends BaseEntity implements GrantedAuthority {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase().replaceAll("\\s+", "_");
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    protected Permission() {
        // framework constructor
    }

    public Permission(String name) {
        setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

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
