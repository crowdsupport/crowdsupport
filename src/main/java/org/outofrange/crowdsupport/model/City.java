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
}
