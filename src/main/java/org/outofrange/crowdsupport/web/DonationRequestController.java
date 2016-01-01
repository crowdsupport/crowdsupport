package org.outofrange.crowdsupport.web;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.rest.dto.CommentDto;
import org.outofrange.crowdsupport.rest.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.security.Security;

@Controller
public class DonationRequestController {
    private static final Logger log = LoggerFactory.getLogger(DonationRequestController.class);

    @Inject
    private ModelMapper mapper;

    @MessageMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/comments")
    public CommentDto sendComment(@DestinationVariable String stateIdentifier, @DestinationVariable String cityIdentifier, @DestinationVariable String placeIdentifier, CommentDto commentDto) {
        log.info("Comment for {}: {}", placeIdentifier, commentDto);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto userDto = mapper.map(user, UserDto.class);

        commentDto.setAuthor(userDto);

        return commentDto;
    }
}
