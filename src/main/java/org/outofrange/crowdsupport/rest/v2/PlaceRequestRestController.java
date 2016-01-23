package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.PlaceDto;
import org.outofrange.crowdsupport.dto.PlaceRequestDto;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.service.PlaceRequestService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/placeRequest")
public class PlaceRequestRestController extends TypeMappingController<PlaceRequestDto> {
    private static final Logger log = LoggerFactory.getLogger(PlaceRequestRestController.class);

    private PlaceRequestService placeRequestService;

    public PlaceRequestRestController(ModelMapper mapper, PlaceRequestService placeRequestService) {
        super(mapper, PlaceRequestDto.class);
        this.placeRequestService = placeRequestService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PlaceRequestDto> getAllPlaceRequestsWithInactivePlaces() {
        log.info("Querying all place requests");

        return mapToList(placeRequestService.loadAllWithInactivePlace());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PlaceRequestDto> createNewPlaceRequest(@RequestBody PlaceRequestDto placeRequestDto) {
        log.info("Requesting new place {}", placeRequestDto);

        final PlaceRequest requestedPlace = placeRequestService.requestNewPlace(map(placeRequestDto, PlaceRequest.class));
        final PlaceRequestDto requestedPlaceDto = map(requestedPlace);

        return ResponseEntity.created(requestedPlaceDto.uri()).body(requestedPlaceDto);
    }

    @RequestMapping(value = "/{requestId}/accept", method = RequestMethod.POST)
    public ResponseEntity<PlaceDto> acceptPlaceRequest(@PathVariable Long requestId, @RequestBody PlaceRequestDto placeRequestDto) {
        log.info("Accepting place request with id {}", requestId);

        final PlaceRequest savedPlaceRequest = placeRequestService.saveNewPlace(map(placeRequestDto, PlaceRequest.class));
        final PlaceDto placeDto = map(savedPlaceRequest.getPlace(), PlaceDto.class);

        return ResponseEntity.created(placeDto.uri()).body(placeDto);
    }

    @RequestMapping(value = "/{requestId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> declinePlaceRequest(@PathVariable Long requestId) {
        log.info("Declining place request with id {}", requestId);

        placeRequestService.declinePlaceRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
