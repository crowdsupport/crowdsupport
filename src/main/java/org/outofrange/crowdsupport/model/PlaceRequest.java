package org.outofrange.crowdsupport.model;

import javax.persistence.*;

@Entity
@Table(name = "PLACE_REQUESTS")
public class PlaceRequest extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "place")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "requesting_user")
    private User requestingUser;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    public PlaceRequest() {

    }

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
