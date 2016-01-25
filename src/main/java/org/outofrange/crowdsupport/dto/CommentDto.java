package org.outofrange.crowdsupport.dto;

import java.time.ZonedDateTime;

/**
 * @author Markus Möslinger
 */
public class CommentDto extends LinkBaseDto {
    private String text;

    private FullUserDto author;

    private ZonedDateTime createdDateTime;

    private int quantity;

    private Long donationRequestId;

    public String getText() {
        return text;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FullUserDto getAuthor() {
        return author;
    }

    public void setAuthor(FullUserDto author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getDonationRequestId() {
        return donationRequestId;
    }

    public void setDonationRequestId(Long donationRequestId) {
        this.donationRequestId = donationRequestId;
    }

    @Override
    protected String self() {
        return "/comment" + getId();
    }
}
