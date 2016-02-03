package org.outofrange.crowdsupport.dto;

public class StatisticsDto {
    private Long states;
    private Long cities;
    private Long places;

    private Long openRequests;
    private Long closedRequests;

    private Long totalUsers;

    public Long getStates() {
        return states;
    }

    public void setStates(Long states) {
        this.states = states;
    }

    public Long getCities() {
        return cities;
    }

    public void setCities(Long cities) {
        this.cities = cities;
    }

    public Long getPlaces() {
        return places;
    }

    public void setPlaces(Long places) {
        this.places = places;
    }

    public Long getOpenRequests() {
        return openRequests;
    }

    public void setOpenRequests(Long openRequests) {
        this.openRequests = openRequests;
    }

    public Long getClosedRequests() {
        return closedRequests;
    }

    public void setClosedRequests(Long closedRequests) {
        this.closedRequests = closedRequests;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
}
