package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A team is a group of {@link User}s managing a {@link Place}.
 */
@Entity
@Table(name = "TEAMS")
public class Team extends BaseEntity {
    /**
     * The place the team is managing.
     */
    @OneToOne
    @JoinColumn(name = "place")
    private Place place;

    /**
     * The members of this team.
     */
    @ManyToMany
    @JoinTable(name = "TEAMS_USERS",
            joinColumns = {@JoinColumn(name = "team", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")})
    private List<User> members = new ArrayList<>();

    protected Team() { /* empty constructor for frameworks */ }

    /**
     * Creates a new team for the place.
     *
     * @param place the place the team is managing.
     * @see #setPlace(Place)
     */
    public Team(Place place) {
        setPlace(place);
    }

    /**
     * Returns the place the team is managing.
     *
     * @return the place the team is managing.
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Sets the place the team is managing.
     *
     * @param place the place the team is managing.
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Returns the members of the team.
     *
     * @return the members of the team.
     */
    public List<User> getMembers() {
        return members;
    }

    /**
     * Sets the members of the team.
     *
     * @param members the members of the team.
     */
    public void setMembers(List<User> members) {
        this.members = members;
    }
}
