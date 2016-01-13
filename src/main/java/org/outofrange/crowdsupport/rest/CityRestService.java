package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.CityDto;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/service/v1/city")
public class CityRestService {
    private static final Logger log = LoggerFactory.getLogger(CityRestService.class);

    @Inject
    private CityService cityService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<CityDto> searchCities(@RequestParam String query) {
        log.debug("Searching cities for {}", query);
        return mapper.mapToList(cityService.searchCities(query), CityDto.class);
    }
}
