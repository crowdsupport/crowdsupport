package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for representing cities.
 * <p>
 * Cities belong to a {@link State} and have {@code 0..n} places belonging to it.
 */
@Entity
@Table(name = "CITIES")
public class City extends BaseEntity {
    /**
     * The state the citiy is in.
     */
    @ManyToOne
    @JoinColumn(name = "state")
    private State state;

    /**
     * The name of the city.
     */
    @Column(name = "name")
    private String name;

    /**
     * The identifier of the city.
     */
    @Column(name = "identifier")
    private String identifier;

    /**
     * A path to an image representing the city
     */
    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Place> places = new ArrayList<>();

    protected City() { /* empty constructor for frameworks */ }

    /**
     * Creates a new instance of the city. The arguments have to follow the same rules as they would've when
     * calling their setter methods.
     *
     * @param state      the state the city is in
     * @param name       the name of the city
     * @param identifier the identifier of the city
     * @see #setState(State)
     * @see #setName(String)
     * @see #setIdentifier(String)
     */
    public City(State state, String name, String identifier) {
        setState(state);
        setName(name);
        setIdentifier(identifier);
    }

    /**
     * Returns the state of the city.
     *
     * @return the state of the city
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the city.
     *
     * @param state the state of the city
     * @throws NullPointerException when {@code state} is null
     */
    public void setState(State state) {
        this.state = Validate.notNull(state);
    }

    /**
     * Returns the name of the city.
     *
     * @return the name of the city.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the city.
     *
     * @param name the name of the city
     * @throws NullPointerException     when {@code name} is null
     * @throws IllegalArgumentException when {@code name} is empty
     */
    public void setName(String name) {
        this.name = Validate.notNullOrEmpty(name);
    }

    /**
     * Returns the image path of the city.
     *
     * @return the image path of the city
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the image path of the city.
     *
     * @param imagePath the image path of the city
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns all places of the city.
     * <p>
     * If the city has no places yet, the returned {@link List} will be empty, but never {@code null}.
     *
     * @return all places of the city
     */
    public List<Place> getPlaces() {
        return places;
    }

    /**
     * Sets the places of the city.
     *
     * @param places the places of the city
     * @throws NullPointerException when {@code places} is null
     */
    public void setPlaces(List<Place> places) {
        this.places = Validate.notNull(places);
    }

    /**
     * Returns the identifier of the city.
     *
     * @return the identifier of the city
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier of the city.
     * <p>
     * The identifier must not be {@code null} or empty, and may only contain lowercase characters.
     *
     * @param identifier the identifier of the city
     * @throws NullPointerException     when {@code identifier} is null
     * @throws IllegalArgumentException when {@code identifier} is empty or has other characters than lowercase letters
     */
    public void setIdentifier(String identifier) {
        this.identifier = Validate.doesntMatch(identifier, ".*[^a-z].*");
    }

    /**
     * Equality of a city is defined by it's {@code state} and it's {@code identifier}.
     *
     * @param o the object to compare equality with
     * @return true if {@code o} is another {@code City} with the same {@code state} and {@code identifier}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return new EqualsBuilder()
                .append(state, city.state)
                .append(identifier, city.identifier)
                .isEquals();
    }

    /**
     * Returns a hash code for this city.
     *
     * @return a hash code for the {@code City}
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(state)
                .append(identifier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "City{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", state=" + state +
                '}';
    }
}
