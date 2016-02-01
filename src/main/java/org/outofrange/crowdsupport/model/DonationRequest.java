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

    @Column(name = "validTo")
    private LocalDateTime validToDateTime;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "units")
    private String units;

    @OneToMany(mappedBy = "donationRequest")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "RequestTags", joinColumns = {@JoinColumn(name = "donationRequest")}, inverseJoinColumns = {@JoinColumn(name = "tag")})
    private List<Tag> tags = new ArrayList<>();

    @Column(name = "resolved")
    private boolean resolved;

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

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
