package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.CityDto;
import org.outofrange.crowdsupport.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/service/v1/city")
public class CityRestService {
    private static final Logger log = LoggerFactory.getLogger(CityRestService.class);

    @Inject
    private CityService cityService;

    @Inject
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<CityDto> searchCities(@RequestParam String query) {
        log.debug("Searching cities for {}", query);

        return cityService.searchCities(query).stream().map(c -> modelMapper.map(c, CityDto.class)).collect(Collectors.toList());
    }
}
