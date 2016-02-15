package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A property is a key/value pair representing an installation specific configuration, read from the database.
 */
@Entity
@Table(name = "PROPERTIES")
public class Property extends BaseEntity {
    /**
     * The immutable key of the property.
     */
    @Column(name = "key")
    private String key;

    /**
     * The value of the property.
     */
    @Column(name = "value")
    private String value;

    protected Property() { /* empty constructor for frameworks */ }

    /**
     * Creates a new property with the given key/value pair.
     *
     * @param key   the key for the new property; will be immutable
     * @param value the value of the property, can be changed later
     */
    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the property.
     *
     * @return the key of the property
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the value of the property.
     *
     * @return the value of the property
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the property.
     *
     * @param value the value of the property
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * A property is equal to another property if they share the same {@code key}.
     *
     * @param o the object to check for equality with
     * @return true if the other property has the same {@code key}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(key, property.key)
                .isEquals();
    }

    /**
     * Returns a hash code for the property.
     *
     * @return a hash code for the property
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(key)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
