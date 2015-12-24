package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.rest.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DonationRequestController {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestController.class);

    @MessageMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/comments")
    public CommentDto sendComment(@DestinationVariable String stateIdentifier, @DestinationVariable String cityIdentifier, @DestinationVariable String placeIdentifier, CommentDto commentDto) {
        log.info("Comment for {}: {}", placeIdentifier, commentDto);

        return commentDto;
    }
}
