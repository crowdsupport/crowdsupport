package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Donations can be brought to a place, which is managed by a {@link Team}.
 */
@Entity
@Table(name = "PLACES")
public class Place extends BaseEntity {
    /**
     * The city the place is in.
     */
    @ManyToOne
    @JoinColumn(name = "city")
    private City city;

    /**
     * A text describing where in the city the place can be found.
     */
    @Column(name = "location")
    private String location;

    /**
     * The name of the place.
     */
    @Column(name = "name")
    private String name;

    /**
     * An identifier for the place.
     */
    @Column(name = "identifier")
    private String identifier;

    /**
     * A path to an image representing the place.
     */
    @Column(name = "imagepath")
    private String imagePath;

    /**
     * A list of associated donation requests for the place.
     */
    @OneToMany(mappedBy = "place")
    private List<DonationRequest> donationRequests = new ArrayList<>();

    /**
     * A boolean value indicating if the place is still active.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * The place request the place has been created with.
     */
    @OneToOne
    @JoinColumn(name = "place_request")
    private PlaceRequest placeRequest;

    /**
     * The team managing the place and accepting donations.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team")
    private Team team;

    protected Place() { /* empty constructor for frameworks */ }

    /**
     * Creates a new place. See respective setter methods for more information.
     *
     * @param city       the city the place is in
     * @param name       the name of the place
     * @param identifier the identifier of the place
     * @param location   the location the place can be found at
     * @see #setCity(City)
     * @see #setName(String)
     * @see #setIdentifier(String)
     * @see #setLocation(String)
     */
    public Place(City city, String name, String identifier, String location) {
        setCity(city);
        setName(name);
        setIdentifier(identifier);
        setLocation(location);
    }

    /**
     * Returns the city the place is in.
     *
     * @return the city the place is in
     */
    public City getCity() {
        return city;
    }

    /**
     * Sets the city the place is in.
     *
     * @param city the city the place is in.
     * @throws NullPointerException if {@code city} is null
     */
    public void setCity(City city) {
        this.city = Validate.notNull(city);
    }

    /**
     * Returns the location the place can be found in the city.
     *
     * @return the location the place can be found in the city
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location the place can be found in the city.
     *
     * @param location the location the place can be found in the city
     * @throws NullPointerException     if {@code location} is null
     * @throws IllegalArgumentException if {@code location} is empty
     */
    public void setLocation(String location) {
        this.location = Validate.notNullOrEmpty(location);
    }

    /**
     * Returns the name of the place.
     *
     * @return the name of the place
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the place.
     *
     * @param name the name of the place
     * @throws NullPointerException     if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty
     */
    public void setName(String name) {
        this.name = Validate.notNullOrEmpty(name);
    }

    /**
     * Returns the image path of the place.
     *
     * @return the image path of the place
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the image path of the place.
     *
     * @param imagePath the image path of the place
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns a list of donation requests created for this place.
     *
     * @return a list of donation requests created for this place
     */
    public List<DonationRequest> getDonationRequests() {
        return donationRequests;
    }

    /**
     * Sets a list of donation requests created for this place.
     *
     * @param donationRequests a list of donation requests created for this place
     * @throws NullPointerException if {@code donationRequests} is null
     */
    public void setDonationRequests(List<DonationRequest> donationRequests) {
        this.donationRequests = Validate.notNull(donationRequests);
    }

    /**
     * Returns the identifier for the place.
     *
     * @return the identifier for the place
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier for the place.
     *
     * @param identifier the identifier for the place
     * @throws NullPointerException     if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty, or contains other characters than lowercase letters
     */
    public void setIdentifier(String identifier) {
        this.identifier = Validate.doesntMatch(identifier, ".*[^a-z].*");
    }

    /**
     * Returns if the place is active, indicating if has been confirmed by an admin already.
     *
     * @return if the place is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets if the place is active, indicating if has been confirmed by an admin already.
     *
     * @param active a boolean value indicating if the place is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the place request the place has been created with.
     *
     * @return the place request the place has been created with
     */
    public PlaceRequest getPlaceRequest() {
        return placeRequest;
    }

    /**
     * Sets the place request the place has been created with.
     *
     * @param placeRequest the place request the place has been created with
     */
    public void setPlaceRequest(PlaceRequest placeRequest) {
        this.placeRequest = placeRequest;
    }

    /**
     * Returns the team of users who are managing the place.
     *
     * @return the team of users who are managing the place
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Sets the team of users who are managing the place.
     *
     * @param team the team of users who are managing the place
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * A place is equal to another place if they share the same {@code identifier} in the same {@code city}.
     *
     * @param o the object to check for equality with
     * @return true if equal
     * @see City#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return new EqualsBuilder()
                .append(city, place.city)
                .append(identifier, place.identifier)
                .isEquals();
    }

    /**
     * Returns a hash code for the place.
     *
     * @return a hash code for the place
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(city)
                .append(identifier)
                .toHashCode();
    }
}
