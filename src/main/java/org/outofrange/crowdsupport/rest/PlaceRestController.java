package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.*;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Team;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/place")
public class PlaceRestController extends TypedMappingController<PlaceDto> {
    private static final Logger log = LoggerFactory.getLogger(PlaceRestController.class);

    private final PlaceService placeService;

    @Inject
    public PlaceRestController(ModelMapper mapper, PlaceService placeService) {
        super(mapper, PlaceDto.class);
        this.placeService = placeService;
    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    public List<PlaceDto> getAllPlacesLike(@RequestParam String query) {
        log.info("Querying places like {}", query);

        return mapToList(placeService.loadPlaceSuggestions(query));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"identifier", "cityIdentifier", "stateIdentifier"})
    public PlaceWithDonationRequestsDto getPlaceWithIdentifier(@RequestParam String identifier, @RequestParam String cityIdentifier,
                                                                   @RequestParam String stateIdentifier) {
        log.info("Querying place in {}/{} named {}", cityIdentifier, stateIdentifier, identifier);

        return map(placeService.load(stateIdentifier, cityIdentifier, identifier).get(), PlaceWithDonationRequestsDto.class);
    }

    @RequestMapping(value = "/{placeId}", method = RequestMethod.GET)
    public PlaceWithDonationRequestsDto getPlace(@PathVariable Long placeId) {
        log.info("Querying place with id {}", placeId);

        return map(placeService.loadPlace(placeId), PlaceWithDonationRequestsDto.class);
    }

    @RequestMapping(value = "/{placeId}/team", method = RequestMethod.GET)
    public List<UserDto> getTeamForPlace(@PathVariable Long placeId) {
        log.info("Querying team for place with id {}", placeId);

        Team team = placeService.loadPlace(placeId).getTeam();
        if (team != null) {
            return mapToList(team.getMembers(), UserDto.class);
        } else {
            return mapToList(Collections.emptyList(), UserDto.class);
        }
    }

    @RequestMapping(value = "/{placeId}/team/{username}", method = RequestMethod.PUT)
    public List<UserDto> addMemberToTeam(@PathVariable Long placeId, @PathVariable String username) {
        log.info("Adding {} to team for place with id {}", username, placeId);

        return mapToList(placeService.addUserToTeam(placeId, username).getTeam().getMembers(), UserDto.class);
    }

    @RequestMapping(value = "/{placeId}/team/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<List<UserDto>> removeMemberFromTeam(@PathVariable Long placeId, @PathVariable String username) {
        log.info("Removing {} from team for place with id {}", username, placeId);

        return ResponseEntity.ok(mapToList(placeService.removeUserFromTeam(placeId, username).getTeam().getMembers(), UserDto.class));
    }

    @RequestMapping(value = "/{placeId}/donationRequests", method = RequestMethod.POST)
    public ResponseEntity<DonationRequestDto> addDonationRequest(@PathVariable Long placeId,
                                                                 @RequestBody DonationRequestDto donationRequestDto) {
        log.info("Adding donation request to place with id {}", placeId);

        final DonationRequest createdDonationRequest = placeService.addDonationRequest(placeId, map(donationRequestDto, DonationRequest.class));
        final DonationRequestDto createdDonationRequestDto = map(createdDonationRequest, DonationRequestDto.class);

        return ResponseEntity.created(createdDonationRequestDto.uri()).body(createdDonationRequestDto);
    }
}
