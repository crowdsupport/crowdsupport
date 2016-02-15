package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A tag can be added to donation requests to categorize them, making it possible to filter for certain attributes.
 */
@Entity
@Table(name = "TAGS")
public class Tag extends BaseEntity {
    /**
     * The name of the tag.
     */
    @Column(name = "name")
    private String name;

    protected Tag() { /* empty constructor for frameworks */ }

    /**
     * Creates a new tag with a given name.
     *
     * @param name the name of the tag
     * @see #setName(String)
     */
    public Tag(String name) {
        setName(name);
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     * <p>
     * Will automatically be converted to lowercase characters.
     *
     * @param name the name of the tag
     * @throws NullPointerException     if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty or contains other characters than letters
     */
    public void setName(String name) {
        String sanitizedName = Validate.notNullOrEmpty(name).toLowerCase();

        if (sanitizedName.matches(".*[^a-zöäü].*")) {
            throw new IllegalArgumentException("Name must only contain lowercase letters, but was " + sanitizedName);
        }

        this.name = sanitizedName;
    }

    /**
     * A tag is equal to another tag if their name is equal.
     *
     * @param o the object to check equality with
     * @return true if their name is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return new EqualsBuilder()
                .append(name, tag.name)
                .isEquals();
    }

    /**
     * Returns a hash code for the tag.
     *
     * @return a hash code for the tag
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
        return getName();
    }
}
