package org.outofrange.crowdsupport.model;

import org.ocpsoft.prettytime.PrettyTime;
import org.outofrange.crowdsupport.util.DateConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "DonationRequests")
public class DonationRequest extends BaseEntity {
    private static final PrettyTime PRETTY_TIME = new PrettyTime(Locale.ENGLISH);

    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "validTo")
    private LocalDateTime validToDateTime;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "donationRequest", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

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

    public int getConfirmedQuantity() {
        return (int)(getPromisedQuantity() * 0.4);
    }

    public float getConfirmedPercentage() {
        return (float)(getConfirmedQuantity()) / getQuantity() * 100;
    }

    public float getPromisedPercentage() {
        return (float)(getPromisedQuantity()) / getQuantity() * 100 - getConfirmedPercentage();
    }

    public String getAge() {
        return PRETTY_TIME.format(DateConverter.toDate(getCreatedDateTime().toLocalDateTime()));
    }

    public String getUntil() {
        return PRETTY_TIME.format(DateConverter.toDate(LocalDateTime.now().plusHours(6)));
    }
}
