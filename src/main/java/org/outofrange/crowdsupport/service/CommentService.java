package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Comment;

public interface CommentService extends BaseService<Comment> {

    Comment addComment(Long placeId, Long donationRequestId, Comment comment);
}
