package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Teams")
public class Team extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "place")
    private Place place;

    @ManyToMany
    @JoinTable(name = "TeamsUsers",
            joinColumns = {@JoinColumn(name = "team", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")})
    private List<User> members = new ArrayList<>();

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
