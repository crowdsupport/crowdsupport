package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.dto.PlaceRequestDto;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.service.PlaceRequestService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Markus Möslinger
 */
@RestController
@RequestMapping(value = "/service/v1")
public class PlaceRestService {
    private static final Logger log = LoggerFactory.getLogger(PlaceRestService.class);

    @Inject
    private PlaceService placeService;

    @Inject
    private PlaceRequestService placeRequestService;

    @Inject
    private CsModelMapper mapper;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/donationRequests",
            method = RequestMethod.GET)
    public List<DonationRequestDto> getDonationRequests(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                                        @PathVariable String placeIdentifier) {
        Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            return mapper.mapToList(place.get().getDonationRequests(), DonationRequestDto.class);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/place/request", method = RequestMethod.POST)
    public ResponseEntity<Void> requestNewPlace(@RequestBody PlaceRequest placeRequest) {
        placeRequest.setRequestingUser(userService.getCurrentUserUpdated().get());
        placeRequestService.requestNewPlace(placeRequest);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/place/request", method = RequestMethod.GET)
    public List<PlaceRequestDto> getAllOpenPlaceRequests() {
        log.info("Requesting all place requests");

        return mapper.mapToList(placeRequestService.loadAllWithInactivePlace(), PlaceRequestDto.class);
    }

    @RequestMapping(value = "/place", method = RequestMethod.POST)
    public ResponseEntity<Void> saveNewPlace(@RequestBody PlaceRequest placeRequest) {
        log.info("Saving previously created new place: {}", placeRequest);

        placeRequestService.saveNewPlace(placeRequest);

        return ResponseEntity.ok().build();
    }
}
