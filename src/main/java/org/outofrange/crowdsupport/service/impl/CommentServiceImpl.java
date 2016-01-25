package org.outofrange.crowdsupport.service.impl;

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
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private SimpMessagingTemplate template;

    @Inject
    private UserService userService;

    @Inject
    private CsModelMapper mapper;

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

        final String topic = "/topic/" + place.getCity().getState().getIdentifier() + "/" +
                place.getCity().getIdentifier() + "/" + place.getIdentifier() + "/comments";
        template.convertAndSend(topic, ChangeDto.add(mapper.map(comment, CommentDto.class)));

        return comment;
    }

    @Override
    public void deleteComment(long commentId) {
        log.debug("Deleting comment with id {}", commentId);

        commentRepository.delete(commentId);
    }
}
