package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A state represents a geographical state and can contain instances of {@link City}s.
 */
@Entity
@Table(name = "STATES")
public class State extends BaseEntity {
    /**
     * The name of the state.
     */
    @Column(name = "name")
    private String name;

    /**
     * The identifier of this state.
     */
    @Column(name = "identifier")
    private String identifier;

    /**
     * The image path of the state.
     */
    @Column(name = "imagepath")
    private String imagePath;

    /**
     * A list of cities in this state.
     */
    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<City> cities = new ArrayList<>();

    protected State() { /* empty constructor for frameworks */ }

    /**
     * Creates a new state with a given name and identifier.
     *
     * @param name       the name of the state
     * @param identifier the identifier of the state
     * @see #setName(String)
     * @see #setIdentifier(String)
     */
    public State(String name, String identifier) {
        setName(name);
        setIdentifier(identifier);
    }

    /**
     * Returns the name of the state.
     *
     * @return the name of the state
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the state.
     *
     * @param name the name of the state
     * @throws NullPointerException if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty
     */
    public void setName(String name) {
        this.name = Validate.notNullOrEmpty(name);
    }

    /**
     * Returns the image path of the state.
     *
     * @return the image path of the state
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the image path of the state.
     *
     * @param imagePath the image path of the state
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns a list of cities in the state.
     *
     * @return a list of cities in the state
     */
    public List<City> getCities() {
        return cities;
    }

    /**
     * Sets the list of cities in the state.
     *
     * @param cities a list of cities in the state
     * @throws NullPointerException if {@code cities} is null
     */
    public void setCities(List<City> cities) {
        this.cities = Validate.notNull(cities);
    }

    /**
     * Returns the identifier of the state.
     *
     * @return the identifier of the state
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier of the state.
     *
     * @param identifier the identifier of the state
     */
    public void setIdentifier(String identifier) {
        this.identifier = Validate.doesntMatch(identifier, ".*[^a-z].*");
    }

    /**
     * A state is equal to another state if they share the same {@code identifier}.
     *
     * @param o the object to check for equality with
     * @return true if they share the same {@code identifier}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return new EqualsBuilder()
                .append(identifier, state.identifier)
                .isEquals();
    }

    /**
     * Returns a hash code for the state.
     *
     * @return a hash code for the state
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(identifier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
