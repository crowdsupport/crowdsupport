package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.outofrange.crowdsupport.spring.api.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/donationRequest")
public class DonationRequestRestController extends MappingController {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestRestController.class);

    private final CommentService commentService;
    private final DonationRequestService donationRequestService;

    @Inject
    public DonationRequestRestController(ModelMapper mapper, CommentService commentService, DonationRequestService donationRequestService) {
        super(mapper);
        this.commentService = commentService;
        this.donationRequestService = donationRequestService;
    }

    @RequestMapping(value = "/{donationRequestId}/comments", method = RequestMethod.POST)
    public ResponseEntity<CommentDto> createCommentOnDonationRequest(@PathVariable Long donationRequestId,
                                                                     @RequestBody CommentDto commentDto) {
        log.info("Creating comment on donation request with id {}", donationRequestId);
        final CommentDto createdComment = map(commentService.addComment(donationRequestId, map(commentDto, Comment.class)),
                CommentDto.class);

        return ResponseEntity.created(createdComment.uri()).body(createdComment);
    }

    @RequestMapping(value = "/{donationRequestId}/resolve", method = RequestMethod.POST)
    public ResponseEntity<Void> resolveDonationRequest(@PathVariable Long donationRequestId) {
        log.info("Resolving donation request with id {}", donationRequestId);

        donationRequestService.setDonationRequestResolved(donationRequestId, true);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{donationRequestId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDonationRequest(@PathVariable Long donationRequestId) {
        log.info("Deleting donation request with id {}", donationRequestId);

        donationRequestService.deleteDonationRequest(donationRequestId);

        return ResponseEntity.ok().build();
    }
}
