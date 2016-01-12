package org.outofrange.crowdsupport.dto;

public class PlaceRequestDto {
    private PlaceDto place;

    private UserDto requestingUser;

    private String state;

    private String city;

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
}
