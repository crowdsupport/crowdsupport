package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DonationRequests")
public class DonationRequest extends BaseEntity {
    private Venue venue;
    private LocalDateTime createdDateTime;
    private LocalDateTime validToDateTime;
    private int quantity;
    private List<Comment> comments;
    private boolean active;
}
