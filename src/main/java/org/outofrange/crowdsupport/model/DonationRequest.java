package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonationRequests")
public class DonationRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "createddatetime")
    private LocalDateTime createdDateTime;

    @Column(name = "validtodatetime")
    private LocalDateTime validToDateTime;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "donationRequest", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Column(name = "active")
    private boolean active;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getValidToDateTime() {
        return validToDateTime;
    }

    public void setValidToDateTime(LocalDateTime validToDateTime) {
        this.validToDateTime = validToDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }

        comment.setDonationRequest(this);

        comments.add(comment);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPromisedQuantity() {
        int promisedQuantity = 0;
        for (Comment comment : comments) {
            promisedQuantity += comment.getQuantity();
        }

        return promisedQuantity;
    }

    public String getAge() {
        return "30 minutes";
    }

    public String getUntil() {
        return "23:59";
    }
}
