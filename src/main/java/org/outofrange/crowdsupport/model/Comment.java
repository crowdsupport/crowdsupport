package org.outofrange.crowdsupport.model;

import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;

/**
 * A {@code Comment} is a comment made by an {@link User} on a specific {@link DonationRequest}.
 * <p>
 * It is meant to tell the team of a place when someone is going to bring donations.
 */
@Entity
@Table(name = "COMMENTS")
public class Comment extends BaseEntity {
    /**
     * The text of the comment.
     */
    @Column(name = "text")
    private String text;

    /**
     * The user who wrote the comment.
     */
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    /**
     * The donation request the comment belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "donation_request")
    private DonationRequest donationRequest;

    /**
     * If the donation request is quantified, {@code quantity} may specify the quantit of the promised donation.
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * A comment can be confirmed by the team to signalize other users that it has been received already.
     */
    @Column(name = "confirmed")
    private boolean confirmed;

    protected Comment() { /* empty constructor for frameworks */ }

    /**
     * Creates a new instance of a comment. See the respective setter methods for constraints on the arguments.
     *
     * @param donationRequest the donation request the comment belongs to.
     * @param author          the user who has written the comment.
     * @param text            the text of the comment.
     * @see #setDonationRequest(DonationRequest)
     * @see #setAuthor(User)
     * @see #setText(String)
     */
    public Comment(DonationRequest donationRequest, User author, String text) {
        setDonationRequest(donationRequest);
        setAuthor(author);
        setText(text);
    }

    /**
     * Returns the text of the comment.
     *
     * @return the text of the comment.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the comment.
     *
     * @param text the text of the comment.
     * @throws NullPointerException     when {@code text} is null.
     * @throws IllegalArgumentException when {@code text} is empty.
     */
    public void setText(String text) {
        this.text = Validate.notNullOrEmpty(text);
    }

    /**
     * Returns the author of the comment.
     *
     * @return the author of the comment.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author of the comment.
     *
     * @param author the author of the comment.
     * @throws NullPointerException when {@code author} is null.
     */
    public void setAuthor(User author) {
        this.author = Validate.notNull(author);
    }

    /**
     * Returns the donation request the comment belongs to.
     *
     * @return the donation request the comment belongs to.
     */
    public DonationRequest getDonationRequest() {
        return donationRequest;
    }

    /**
     * Sets the donation request the comment belongs to.
     *
     * @param donationRequest the donation request the comment belongs to.
     * @throws NullPointerException when {@code donationRequest} is null.
     */
    public void setDonationRequest(DonationRequest donationRequest) {
        this.donationRequest = Validate.notNull(donationRequest);
    }

    /**
     * Returns the quantity of the comment. Is 0 if the comment hasn't been quantified.
     *
     * @return the quantity of the comment.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the comment. Other values than {@code 0} are only allowed if the {@code donationRequest}
     * of the comment is quantified by itself.
     *
     * @param quantity the quantity of the comment
     * @throws IllegalArgumentException when quantity is either negative, or greater than zero, while having
     *                                  no {@code donationRequest} at all, or an unquantified one.
     */
    public void setQuantity(int quantity) {
        Validate.greaterOrEqualZero(quantity);

        if (quantity > 0 && (donationRequest == null || donationRequest.getQuantity() == 0)) {
            throw new IllegalArgumentException("Comments can only have a quantity other than zero if they have a " +
                    "quantified donation request!");
        }

        this.quantity = quantity;
    }

    /**
     * Returns if the comment has been confirmed.
     *
     * @return if the comment has been confirmed.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets if the comment has been confirmed.
     *
     * @param confirmed a boolean value indicating if the comment has been confirmed.
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
