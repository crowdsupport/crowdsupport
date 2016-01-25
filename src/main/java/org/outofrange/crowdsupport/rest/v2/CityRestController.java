package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.CityDto;
import org.outofrange.crowdsupport.dto.CityWithPlacesDto;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/city")
public class CityRestController extends TypeMappingController<CityDto> {
    private static final Logger log = LoggerFactory.getLogger(CityRestController.class);

    private final CityService cityService;

    @Inject
    public CityRestController(ModelMapper mapper, CityService cityService) {
        super(mapper, CityDto.class);
        this.cityService = cityService;
    }

    @RequestMapping(method = RequestMethod.GET, params = "!query")
    public List<CityDto> getAllCities() {
        log.info("Querying all cities");

        return mapToList(cityService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    public List<CityDto> getAllCitiesLike(@RequestParam String query) {
        log.info("Querying all cities like {}", query);

        return mapToList(cityService.searchCities(query));
    }

    @RequestMapping(value = "/{cityId}", method = RequestMethod.GET)
    public CityWithPlacesDto getCity(@PathVariable Long cityId) {
        log.info("Querying city with id {}", cityId);

        return map(cityService.load(cityId), CityWithPlacesDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        log.info("Creating city {}", cityDto);

        final CityDto createdCity = map(cityService.createCity(cityDto.getName(), cityDto.getIdentifier(),
                cityDto.getImagePath(), cityDto.getState().getIdentifier()));

        return ResponseEntity.created(createdCity.uri()).body(createdCity);
    }
}
