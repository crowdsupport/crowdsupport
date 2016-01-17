package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.StateDto;
import org.outofrange.crowdsupport.dto.StateWithCitiesDto;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.StateService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<StateDto> searchStates(@RequestParam(required = false) String query) {
        if (query != null) {
            log.info("Searching states for {}", query);
            return mapper.mapToList(stateService.searchStates(query), StateDto.class);
        } else {
            log.info("Retrieving all states");
            return mapper.mapToList(stateService.loadAll(), StateDto.class);
        }
    }

    @RequestMapping(value = "/{stateIdentifier}", method = RequestMethod.GET)
    public StateWithCitiesDto getState(@PathVariable String stateIdentifier) {
        log.info("Requesting state with id {}", stateIdentifier);
        return mapper.map(stateService.load(stateIdentifier).get(), StateWithCitiesDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StateDto> createState(@RequestBody StateDto stateDto) {
        log.info("Creating state {}", stateDto);

        final State createdState = stateService.createState(stateDto.getName(), stateDto.getIdentifier(), stateDto.getIdentifier());

        return ResponseEntity.ok(mapper.map(createdState, StateDto.class));
    }
}
