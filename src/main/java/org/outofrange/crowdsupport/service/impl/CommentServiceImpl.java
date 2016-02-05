package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final DonationRequestRepository donationRequestRepository;
    private final UserService userService;

    @Inject
    public CommentServiceImpl(CommentRepository commentRepository, DonationRequestRepository donationRequestRepository,
                              UserService userService) {
        this.commentRepository = commentRepository;
        this.donationRequestRepository = donationRequestRepository;
        this.userService = userService;
    }

    @Override
    public List<Comment> loadAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment addComment(long donationRequestId, Comment comment) {
        log.debug("Comment for donation request with id {}: {}", donationRequestId, comment);

        User user = userService.getCurrentUserUpdated().get();

        final DonationRequest donationRequest = donationRequestRepository.getOne(donationRequestId);
        comment.setAuthor(user);
        comment.setDonationRequest(donationRequest);
        comment = commentRepository.save(comment);

        Events.comment(ChangeType.CREATE, comment).publish();

        return comment;
    }

    @Override
    public void deleteComment(long commentId) {
        log.debug("Deleting comment with id {}", commentId);

        final Comment comment = commentRepository.findOne(commentId);
        commentRepository.delete(comment);

        Events.comment(ChangeType.DELETE, comment).publish();
    }

    @Override
    public void setCommentConfirmed(long commentId, boolean confirmed) {
        log.debug("Setting confirmed of comment with id {} to {}", commentId, confirmed);

        Comment comment = commentRepository.findOne(commentId);
        comment.setConfirmed(confirmed);
        comment = commentRepository.save(comment);

        Events.comment(ChangeType.UPDATE, comment).publish();
    }
}
