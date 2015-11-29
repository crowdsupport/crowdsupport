package org.outofrange.crowdsupport.model;

import javax.persistence.*;

@Entity
@Table(name = "Comments")
public class Comment extends BaseEntity {
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "donationrequest")
    private DonationRequest donationRequest;
}
