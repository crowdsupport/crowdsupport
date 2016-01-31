package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(long donationRequestId, Comment comment);

    void deleteComment(long commentId);

    Comment save(Comment entity);

    List<Comment> loadAll();

    void setCommentConfirmed(long commentId, boolean confirmed);
}
