package org.outofrange.crowdsupport.rest.dto;

import java.time.ZonedDateTime;

/**
 * @author Markus Möslinger
 */
public class CommentDto {
    private String text;

    private UserDto author;

    private ZonedDateTime createdDateTime;

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

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }
}
