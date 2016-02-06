package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLACES")
public class Place extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "city")
    private City city;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "place")
    private List<DonationRequest> donationRequests = new ArrayList<>();

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "active")
    private boolean active;

    @OneToOne
    @JoinColumn(name = "place_request")
    private PlaceRequest placeRequest;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team")
    private Team team;

    protected Place() { /* empty constructor for frameworks */ }

    public Place(City city, String location, String name, String identifier) {
        setCity(city);
        setLocation(location);
        setName(name);
        setIdentifier(identifier);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = Validate.notNull(city);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Validate.notNullOrEmpty(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Validate.notNullOrEmpty(name);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<DonationRequest> getDonationRequests() {
        return donationRequests;
    }

    public void setDonationRequests(List<DonationRequest> donationRequests) {
        this.donationRequests = Validate.notNull(donationRequests);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = Validate.doesntMatch(identifier, ".*[^a-z].*");
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PlaceRequest getPlaceRequest() {
        return placeRequest;
    }

    public void setPlaceRequest(PlaceRequest placeRequest) {
        this.placeRequest = placeRequest;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(city)
                .append(identifier)
                .toHashCode();
    }
}
