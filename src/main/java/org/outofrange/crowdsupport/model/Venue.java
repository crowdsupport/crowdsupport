package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Venues")
public class Venue extends BaseEntity {
    @ManyToOne
    @JoinTable(name = "city")
    private City city;

    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagePath;

    @OneToMany(mappedBy = "venue")
    private List<DonationRequest> donationRequests;
}
