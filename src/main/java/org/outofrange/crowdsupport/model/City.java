package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Cities")
public class City extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "state")
    private State state;

    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "city")
    private List<Venue> venues;

    @Column(name = "identifier")
    private String identifier;

    public City() {
    }

    public City(State state, String name) {
        this.state = state;
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll("\\s+", "");
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
