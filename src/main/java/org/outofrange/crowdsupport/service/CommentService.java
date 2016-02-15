package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Comment;

import java.util.List;

public interface CommentService {
    /**
     * Adds a comment to a donation request.
     *
     * @param donationRequestId the donation request id where the comment should be added to
     * @param comment           the comment that should be added
     * @return the added comment
     * @throws ServiceException if no donation request could be found
     */
    Comment addComment(long donationRequestId, Comment comment);

    /**
     * Deletes a comment if it exists. Otherwise, it does nothing.
     *
     * @param commentId the id of the comment
     */
    void deleteComment(long commentId);

    /**
     * Loads all persisted comments
     *
     * @return all comments
     */
    List<Comment> loadAll();

    /**
     * Sets a comment to confirmed.
     *
     * @param commentId the comment id
     * @param confirmed if it should be confirmed or not
     * @throws ServiceException if no comment could be found
     */
    void setCommentConfirmed(long commentId, boolean confirmed);
}
