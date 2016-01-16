package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Place> places = new ArrayList<>();

    @Column(name = "identifier")
    private String identifier;

    public City() {
    }

    public City(State state, String name) {
        this.state = state;
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll("\\s+", "");
    }

    public City(State state, String name, String identifier, String imagePath) {
        this.state = state;
        this.name = name;
        this.identifier = identifier;
        this.imagePath = imagePath;
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return new EqualsBuilder()
                .append(state, city.state)
                .append(name, city.name)
                .append(imagePath, city.imagePath)
                .append(identifier, city.identifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(state)
                .append(name)
                .append(imagePath)
                .append(identifier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "City{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", state=" + state +
                '}';
    }
}
