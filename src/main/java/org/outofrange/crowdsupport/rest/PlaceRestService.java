package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.*;
import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.service.*;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Markus Möslinger
 */
@RestController
@RequestMapping(value = "/service/v1/place")
public class PlaceRestService {
    private static final Logger log = LoggerFactory.getLogger(PlaceRestService.class);

    @Inject
    private PlaceService placeService;

    @Inject
    private PlaceRequestService placeRequestService;

    @Inject
    private DonationRequestService donationRequestService;

    @Inject
    private CsModelMapper mapper;

    @Inject
    private UserService userService;

    @Inject
    private CommentService commentService;

    @Inject
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/donationRequests", method = RequestMethod.GET)
    public List<DonationRequestDto> getDonationRequests(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                                        @PathVariable String placeIdentifier) {
        Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            return mapper.mapToList(place.get().getDonationRequests(), DonationRequestDto.class);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{placeId}/donationRequests", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<Void>> addDonationRequest(@PathVariable Long placeId, @RequestBody DonationRequestDto donationRequestDto) {
        log.info("Adding new donation request {}", donationRequestDto);

        placeService.addDonationRequest(placeId, mapper.map(donationRequestDto, DonationRequest.class));

        return ResponseEntity.ok(ResponseDto.success("Successfully added new donation request"));
    }

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/member", method = RequestMethod.GET)
    public List<UserDto> getTeamMembers(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                        @PathVariable String placeIdentifier) {
        log.info("Requesting all users for place {}", placeIdentifier);

        final Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            return mapper.mapToList(place.get().getTeam().getMembers(), UserDto.class);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/member", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<Void>> addTeamMember(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                        @PathVariable String placeIdentifier, @RequestBody String username) {
        log.info("Adding team member {} to place {}", username, placeIdentifier);

        final Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            placeService.addUserToTeam(place.get(), username);
            return ResponseEntity.ok(ResponseDto.success("Added user " + username + " successfully to place"));
        } else {
            return ResponseEntity.badRequest().body(ResponseDto.error("Couldn't add user " + username + " to place"));
        }
    }

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/member/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseDto<Void>> removeTeamMember(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                                           @PathVariable String placeIdentifier, @PathVariable String username) {
        log.info("Removing team member {} to place {}", username, placeIdentifier);

        final Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            placeService.removeUserFromTeam(place.get(), username);
            return ResponseEntity.ok(ResponseDto.success("Removed user " + username + " successfully from place"));
        } else {
            return ResponseEntity.badRequest().body(ResponseDto.error("Couldn't remove user " + username + " from place"));
        }
    }

    @RequestMapping(value = "/{identifier}")
    public ResponseEntity<PlaceWithDonationRequestsDto> getPlace(@PathVariable String identifier,
                                                                 @RequestParam String cityIdentifier,
                                                                 @RequestParam String stateIdentifier) {

        return ResponseEntity.ok(mapper.map(placeService.load(stateIdentifier, cityIdentifier, identifier).get(),
                PlaceWithDonationRequestsDto.class));
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity<Void> requestNewPlace(@RequestBody PlaceRequest placeRequest) {
        placeRequest.setRequestingUser(userService.getCurrentUserUpdated().get());
        placeRequestService.requestNewPlace(placeRequest);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public List<PlaceRequestDto> getAllOpenPlaceRequests() {
        log.info("Requesting all place requests");

        return mapper.mapToList(placeRequestService.loadAllWithInactivePlace(), PlaceRequestDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PlaceDto> saveNewPlace(@RequestBody PlaceRequest placeRequest) {
        log.info("Saving previously created new place: {}", placeRequest);

        final PlaceRequest savedPlaceRequest = placeRequestService.saveNewPlace(placeRequest);

        return ResponseEntity.ok(mapper.map(savedPlaceRequest.getPlace(), PlaceDto.class));
    }

    @RequestMapping(value = "/request/{requestId}/decline", method = RequestMethod.POST)
    public ResponseEntity<Void> declinePlaceRequest(@PathVariable Long requestId) {
        log.info("Declining place request with id {}", requestId);

        placeRequestService.declinePlaceRequest(requestId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{placeId}/comments", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<Void>> addComment(@PathVariable Long placeId, @RequestBody CommentDto commentDto) {
        log.info("Comment for place with id {}: {}", placeId, commentDto);

        commentService.addComment(placeId, commentDto.getDonationRequestId(), mapper.map(commentDto, Comment.class));

        return ResponseEntity.ok(ResponseDto.success("Successfully posted comment"));
    }
}
