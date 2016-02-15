package org.outofrange.crowdsupport.model;

import org.outofrange.crowdsupport.util.Validate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A donation request is created in a {@link Place} the ask users for a donation.
 */
@Entity
@Table(name = "DONATION_REQUESTS")
public class DonationRequest extends BaseEntity {
    /**
     * The place the donation request belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "place")
    private Place place;

    /**
     * The title of the donation request.
     */
    @Column(name = "title")
    private String title;

    /**
     * A text describing the donation request.
     */
    @Column(name = "description")
    private String description;

    /**
     * A datetime describing until when the donations are needed.
     */
    @Column(name = "valid_to")
    private ZonedDateTime validToDateTime;

    /**
     * A number indicating how many donations are needed.
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * Specifies in what units the {@link #quantity} is measured.
     */
    @Column(name = "units")
    private String units;

    /**
     * The comments written on the donation request.
     */
    @OneToMany(mappedBy = "donationRequest")
    private List<Comment> comments = new ArrayList<>();

    /**
     * The tags the donation request has been created with.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "DONATION_REQUESTS_TAGS", joinColumns = {@JoinColumn(name = "donation_request")}, inverseJoinColumns = {@JoinColumn(name = "tag")})
    private List<Tag> tags = new ArrayList<>();

    /**
     * When a donation request is resolved, no further donations are needed.
     */
    @Column(name = "resolved")
    private boolean resolved;

    protected DonationRequest() { /* empty constructor for frameworks */ }

    /**
     * Creates a new donation request. See the respective setter methods for further information.
     *
     * @param place       the place of the donation request.
     * @param title       the title of the donation request.
     * @param description the description of the donation request.
     * @see #setPlace(Place)
     * @see #setTitle(String)
     * @see #setDescription(String)
     */
    public DonationRequest(Place place, String title, String description) {
        setPlace(place);
        setTitle(title);
        setDescription(description);
        setResolved(false);
    }

    /**
     * Returns the place of the donation request.
     *
     * @return the place of the donation request.
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Sets the place of a donation request.
     *
     * @param place the place of a donation request.
     * @throws NullPointerException when {@code place} is null.
     */
    public void setPlace(Place place) {
        this.place = Validate.notNull(place);
    }

    /**
     * Returns the title of the donation request.
     *
     * @return the title of the donation request.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the donation request.
     *
     * @param title the title of the donation request.
     * @throws NullPointerException     when {@code title} is null.
     * @throws IllegalArgumentException when {@code title} is empty.
     */
    public void setTitle(String title) {
        this.title = Validate.notNullOrEmpty(title);
    }

    /**
     * Returns the description of the donation request.
     *
     * @return the description of the donation request.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the donation request.
     *
     * @param description the description of the donation request.
     * @throws NullPointerException     when {@code description} is null.
     * @throws IllegalArgumentException when {@code description} is empty.
     */
    public void setDescription(String description) {
        this.description = Validate.notNullOrEmpty(description);
    }

    /**
     * Returns until when the donation request is valid.
     *
     * @return until when the donation request is valid.
     */
    public ZonedDateTime getValidToDateTime() {
        return validToDateTime;
    }

    /**
     * Sets until when the donation request is valid.
     * <p>
     * Indicates that donations after this date aren't needed anymore.
     *
     * @param validToDateTime a date until when the donation request is valid.
     */
    public void setValidToDateTime(ZonedDateTime validToDateTime) {
        this.validToDateTime = validToDateTime;
    }

    /**
     * Returns the quantity of the donation request.
     *
     * @return the quantity of the donation request.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the donation request.
     * <p>
     * Indicates how much donations are needed.
     *
     * @param quantity the quantity of the donation request.
     * @throws IllegalArgumentException when {@code quantity} is negative.
     */
    public void setQuantity(int quantity) {
        Validate.greaterOrEqualZero(quantity);

        this.quantity = quantity;
    }

    /**
     * Returns a list of the comments made on this donation request.
     *
     * @return a list of the comments made on this donation request.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Adds a comment to the donation request, and sets the donation request on the comment.
     *
     * @param comment the comment to add to the donation request.
     * @throws NullPointerException if {@code comment} is null
     */
    public void addComment(Comment comment) {
        Validate.notNull(comment);

        if (comments == null) {
            comments = new ArrayList<>();
        }

        comment.setDonationRequest(this);

        comments.add(comment);
    }

    /**
     * Returns if the donation request has been resolved, indicating that no further donations are needed.
     * <p>
     * A newly created donation request is unresolved by default.
     *
     * @return a boolean value indicating if the donation request has been resolved.
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets the resolved value for a donation request.
     *
     * @param resolved a boolean value indicating if the request has been resolved.
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * Returns the units the {@code quantity} is measured in.
     *
     * @return the units the {@code quantity} is measured in.
     */
    public String getUnits() {
        return units;
    }

    /**
     * Sets the units the {@code quantity} is measured in.
     *
     * @param units the units the {@code quantity} is measured in.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Returns a list of associated tags with this donation request.
     *
     * @return a list of associated tags with this donation request.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets a list of associated tags with this donation request.
     *
     * @param tags a list of associated tags with this donation request.
     * @throws NullPointerException when {@code tags} is null.
     */
    public void setTags(List<Tag> tags) {
        this.tags = Validate.notNull(tags);
    }
}
