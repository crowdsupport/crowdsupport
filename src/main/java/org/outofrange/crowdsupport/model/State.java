package org.outofrange.crowdsupport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "States")
public class State extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "state")
    private List<City> cities;

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
}
