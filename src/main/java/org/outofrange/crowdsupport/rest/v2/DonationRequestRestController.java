package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/donationRequest")
public class DonationRequestRestController extends MappingController {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestRestController.class);

    private final CommentService commentService;

    @Inject
    public DonationRequestRestController(ModelMapper mapper, CommentService commentService) {
        super(mapper);
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{donationRequestId}/comments", method = RequestMethod.POST)
    public ResponseEntity<CommentDto> createCommentOnDonationRequest(@PathVariable Long donationRequestId,
                                                                     @RequestBody CommentDto commentDto) {
        log.info("Creating comment on donation request with id {}", donationRequestId);
        final CommentDto createdComment = map(commentService.addComment(donationRequestId, map(commentDto, Comment.class)),
                CommentDto.class);

        return ResponseEntity.created(createdComment.uri()).body(createdComment);
    }
}
