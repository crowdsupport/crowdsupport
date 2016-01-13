package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.StateDto;
import org.outofrange.crowdsupport.service.StateService;
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
@RequestMapping(value = "/service/v1/state")
public class StateRestService {
    private static final Logger log = LoggerFactory.getLogger(StateRestService.class);

    @Inject
    private StateService stateService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<StateDto> searchStates(@RequestParam String query) {
        log.debug("Searching states for {}", query);
        return mapper.mapToList(stateService.searchStates(query), StateDto.class);
    }
}
