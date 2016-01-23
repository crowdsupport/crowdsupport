package org.outofrange.crowdsupport.dto;

import java.time.ZonedDateTime;

public class PlaceRequestDto extends LinkBaseDto {
    private Long id;

    private PlaceDto place;

    private UserDto requestingUser;

    private String state;

    private String city;

    private ZonedDateTime createdDateTime;

    public PlaceDto getPlace() {
        return place;
    }

    public void setPlace(PlaceDto place) {
        this.place = place;
    }

    public UserDto getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(UserDto requestingUser) {
        this.requestingUser = requestingUser;
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

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected String self() {
        return "/placeRequest/" + getId();
    }
}
