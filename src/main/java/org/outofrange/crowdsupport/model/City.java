package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CITIES")
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

    protected City() { /* empty constructor for frameworks */ }

    public City(State state, String name, String identifier) {
        setState(state);
        setName(name);
        setIdentifier(identifier);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = Validate.notNull(state);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Validate.notNullOrEmpty(name);
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
        this.places = Validate.notNull(places);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = Validate.doesntMatch(identifier, ".*[^a-z].*");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return new EqualsBuilder()
                .append(state, city.state)
                .append(identifier, city.identifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(state)
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
