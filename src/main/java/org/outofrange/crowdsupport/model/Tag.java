package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TAGS")
public class Tag extends BaseEntity {
    @Column(name = "name")
    private String name;

    protected Tag() { /* empty constructor for frameworks */ }

    public Tag(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String sanitizedName = Validate.notNullOrEmpty(name).toLowerCase();

        if (sanitizedName.matches(".*[^a-zöäü].*")) {
            throw new IllegalArgumentException("Name must only contain lowercase letters, but was " + sanitizedName);
        }

        this.name = sanitizedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return new EqualsBuilder()
                .append(name, tag.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
