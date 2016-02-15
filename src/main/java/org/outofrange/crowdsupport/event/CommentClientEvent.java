package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.Comment;

/**
 * An Event that notifies clients about a new comment.
 */
public class CommentClientEvent extends ClientEntityChangeEvent<Comment> {
    public CommentClientEvent(ChangeType changeType, Comment comment, String... furtherTopics) {
        super(changeType, comment, "comments", furtherTopics);

        if (comment.getQuantity() != 0) {
            if (changeType == ChangeType.CREATE) {
                addEvent(new SimpleClientEvent(comment.getQuantity(), "comments/quantity"));
            } else if (changeType == ChangeType.DELETE) {
                addEvent(new SimpleClientEvent(-comment.getQuantity(), "comments/quantity"));
            }
        }
    }
}
