package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Venues")
public class Place extends BaseEntity {
    @ManyToOne
    @JoinTable(name = "city")
    private City city;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<DonationRequest> donationRequests = new ArrayList<>();

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "active")
    private boolean active;

    @OneToOne
    @JoinColumn(name = "placeRequest")
    private PlaceRequest placeRequest;

    @OneToOne
    @JoinColumn(name = "team")
    private Team team;

    public Place() {
    }

    public Place(City city, String location, String name) {
        this.city = city;
        this.location = location;
        this.name = name;

        this.identifier = name.toLowerCase().replaceAll("\\s+", "");
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.donationRequests = donationRequests;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
}
