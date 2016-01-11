package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.outofrange.crowdsupport.service.PlaceRequestService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Markus Möslinger
 */
@RestController
@RequestMapping(value = "/service/v1")
public class PlaceRestService {
    private static final Logger log = LoggerFactory.getLogger(PlaceRestService.class);

    @Inject
    private DonationRequestService donationRequestService;

    @Inject
    private PlaceService placeService;

    @Inject
    private PlaceRequestService placeRequestService;

    @Inject
    private ModelMapper mapper;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}/donationRequests",
            method = RequestMethod.GET)
    public List<DonationRequestDto> getDonationRequests(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                                     @PathVariable String placeIdentifier) {
        Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            return place.get().getDonationRequests().stream().map(r -> mapper.map(r, DonationRequestDto.class)).collect(Collectors.toList());
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
}
