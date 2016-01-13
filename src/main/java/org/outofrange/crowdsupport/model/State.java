package org.outofrange.crowdsupport.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "States")
public class State extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<City> cities = new ArrayList<>();

    @Column(name = "identifier")
    private String identifier;

    public State() {

    }

    public State(String name) {
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll("\\s+", "");
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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
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

        State state = (State) o;

        return new EqualsBuilder()
                .append(name, state.name)
                .append(imagePath, state.imagePath)
                .append(identifier, state.identifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(imagePath)
                .append(identifier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
