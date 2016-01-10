package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.service.DonationRequestService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Markus Möslinger
 */
@RestController
@RequestMapping(value = "/service/v1/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}")
public class PlaceRestService {
    @Inject
    private DonationRequestService donationRequestService;

    @Inject
    private PlaceService placeService;

    @Inject
    private ModelMapper mapper;

    @RequestMapping(value = "/donationRequests", method = RequestMethod.GET)
    public List<DonationRequestDto> getDonationRequests(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                                     @PathVariable String placeIdentifier) {
        Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);

        if (place.isPresent()) {
            return place.get().getDonationRequests().stream().map(r -> mapper.map(r, DonationRequestDto.class)).collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
