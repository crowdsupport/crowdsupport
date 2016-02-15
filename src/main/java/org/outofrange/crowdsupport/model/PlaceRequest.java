package org.outofrange.crowdsupport.model;

import javax.persistence.*;

/**
 * A request for a new place can be created by users. Before the place can be seen by other users, it has to be
 * accepted by an admin.
 */
@Entity
@Table(name = "PLACE_REQUESTS")
public class PlaceRequest extends BaseEntity {
    /**
     * The requested place.
     */
    @OneToOne
    @JoinColumn(name = "place")
    private Place place;

    /**
     * The user who has requested the place.
     */
    @ManyToOne
    @JoinColumn(name = "requesting_user")
    private User requestingUser;

    /**
     * If the place is requested for an unknown state, the name of the state will be stored here.
     */
    @Column(name = "state")
    private String state;

    /**
     * If the place is requested for an unknown city, the name of the city will be stored here.
     */
    @Column(name = "city")
    private String city;

    protected PlaceRequest() { /* empty constructor for frameworks */ }

    /**
     * Creates a new place request.
     *
     * @param place          the place to request
     * @param requestingUser the user who is requesting the place
     * @see #setPlace(Place)
     * @see #setRequestingUser(User)
     */
    public PlaceRequest(Place place, User requestingUser) {
        setPlace(place);
        setRequestingUser(requestingUser);
    }

    /**
     * Returns the requesting user.
     *
     * @return the requesting user
     */
    public User getRequestingUser() {
        return requestingUser;
    }

    /**
     * Sets the requesting user.
     *
     * @param requestingUser the requesting user
     */
    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    /**
     * Returns the requested place.
     *
     * @return the requested place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Sets the requested place.
     *
     * @param place the requested place
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Returns the requested state the place should be in, if it's in an unknown state.
     *
     * @return the state the place should be in, or null if the state is already persisted
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state the requested place should be in, if the state is not yet known.
     *
     * @param state the state the requested place should be in
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the requested city the place should be in, if it's in an unknown city.
     *
     * @return the city the place should be in, or null if the city is already persisted
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city the requested place should be in, if the city is not yet known.
     *
     * @param city the city the requested place should be in
     */
    public void setCity(String city) {
        this.city = city;
    }
}
