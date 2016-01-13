package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.CityDto;
import org.outofrange.crowdsupport.model.City;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/service/v1/city")
public class CityRestService {
    private static final Logger log = LoggerFactory.getLogger(CityRestService.class);

    private static final Pattern CITY_WITH_STATE = Pattern.compile("(.+) \\(.+\\)");

    @Inject
    private CityService cityService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<CityDto> searchCities(@RequestParam String query) {
        log.info("Searching cities for {}", query);

        List<City> cities = cityService.searchCities(query);
        if (cities.size() == 0) {
            final Matcher m = CITY_WITH_STATE.matcher(query);
            if (m.matches()) {
                final String withoutParenthesis = m.group(1);
                log.debug("Possible search after browser autocomplete, trying again without parenthesis: {}", withoutParenthesis);
                cities = cityService.searchCities(withoutParenthesis);
            }
        }

        return mapper.mapToList(cities, CityDto.class);
    }
}
