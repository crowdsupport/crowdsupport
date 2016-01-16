package org.outofrange.crowdsupport.dto;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class DonationRequestDto extends BaseDto {
    private Long id;

    private String title;

    private String description;

    private ZonedDateTime createdDateTime;

    private ZonedDateTime validToDateTime;

    private int quantity;

    private List<CommentDto> comments;

    private boolean active;

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

    public ZonedDateTime getValidToDateTime() {
        return validToDateTime;
    }

    public void setValidToDateTime(ZonedDateTime validToDateTime) {
        this.validToDateTime = validToDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
