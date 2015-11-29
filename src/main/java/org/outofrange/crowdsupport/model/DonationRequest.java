package org.outofrange.crowdsupport.model;

import java.time.LocalDateTime;
import java.util.List;

public class DonationRequest extends IdentifiedEntity {
    private Venue venue;
    private LocalDateTime createdDateTime;
    private LocalDateTime validToDateTime;
    private int quantity;
    private List<Comment> comments;
    private boolean active;
}
