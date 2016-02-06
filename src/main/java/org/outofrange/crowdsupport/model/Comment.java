package org.outofrange.crowdsupport.model;

import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;

@Entity
@Table(name = "COMMENTS")
public class Comment extends BaseEntity {
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "donation_request")
    private DonationRequest donationRequest;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "confirmed")
    private boolean confirmed;

    protected Comment() { /* empty constructor for frameworks */ }

    public Comment(DonationRequest donationRequest, User author, String text) {
        setDonationRequest(donationRequest);
        setAuthor(author);
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Validate.notNullOrEmpty(text);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = Validate.notNull(author);
    }

    public DonationRequest getDonationRequest() {
        return donationRequest;
    }

    public void setDonationRequest(DonationRequest donationRequest) {
        this.donationRequest = Validate.notNull(donationRequest);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
