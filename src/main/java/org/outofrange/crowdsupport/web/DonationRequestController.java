package org.outofrange.crowdsupport.web;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller
public class DonationRequestController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestController.class);

    @Inject
    private ModelMapper mapper;

    @Inject
    private CommentService commentService;

    @Inject
    private DonationRequestService donationRequestService;

    @MessageMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/comments")
    public ChangeDto<CommentDto> sendComment(@DestinationVariable String stateIdentifier, @DestinationVariable String cityIdentifier, @DestinationVariable String placeIdentifier, CommentDto commentDto) {
        log.info("Comment for {}: {}", placeIdentifier, commentDto);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment comment = mapper.map(commentDto, Comment.class);

        comment.setAuthor(user);
        comment.setDonationRequest(donationRequestService.loadWithId(commentDto.getDonationRequestId()).get());
        commentService.save(comment);

        return ChangeDto.add(mapper.map(comment, CommentDto.class));
    }
}
