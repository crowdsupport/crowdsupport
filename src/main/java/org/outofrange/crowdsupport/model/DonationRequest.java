package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DonationRequests")
public class DonationRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    @Column(name = "createddatetime")
    private LocalDateTime createdDateTime;

    @Column(name = "validtodatetime")
    private LocalDateTime validToDateTime;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "donationRequest")
    private List<Comment> comments;

    @Column(name = "active")
    private boolean active;
}
