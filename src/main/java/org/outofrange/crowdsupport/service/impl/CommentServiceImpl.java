package org.outofrange.crowdsupport.service.impl;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Inject
    private UserService userService;

    @Inject
    private ModelMapper mapper;

    @Inject
    private WebSocketService webSocketService;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
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
        final Place place = donationRequest.getPlace();

        comment.setAuthor(user);
        comment.setDonationRequest(donationRequest);
        comment = commentRepository.save(comment);

        webSocketService.sendChangeToPlace(ChangeDto.add(mapper.map(comment, CommentDto.class)), place);

        return comment;
    }

    @Override
    public void deleteComment(long commentId) {
        log.debug("Deleting comment with id {}", commentId);

        final Comment comment = commentRepository.findOne(commentId);
        final Place place = comment.getDonationRequest().getPlace();

        commentRepository.delete(comment);

        webSocketService.sendChangeToPlace(ChangeDto.remove(commentId, CommentDto.class), place);
    }

    @Override
    public void setCommentConfirmed(long commentId, boolean confirmed) {
        log.debug("Setting confirmed of comment with id {} to {}", commentId, confirmed);

        Comment comment = commentRepository.findOne(commentId);
        final Place place = comment.getDonationRequest().getPlace();

        comment.setConfirmed(confirmed);

        comment = commentRepository.save(comment);
        webSocketService.sendChangeToPlace(ChangeDto.refresh(mapper.map(comment, CommentDto.class)), place);
    }
}
