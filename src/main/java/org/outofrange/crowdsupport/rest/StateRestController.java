package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.StateDto;
import org.outofrange.crowdsupport.dto.StateWithCitiesDto;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.StateService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/state")
public class StateRestController extends TypedMappingController<StateDto> {
    private static final Logger log = LoggerFactory.getLogger(StateRestController.class);

    private final StateService stateService;

    @Inject
    public StateRestController(ModelMapper mapper, StateService stateService) {
        super(mapper, StateDto.class);
        this.stateService = stateService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"!query", "!identifier"})
    public List<StateDto> getAllStates() {
        log.info("Querying all states");

        return mapToList(stateService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    public List<StateDto> getAllStatesLike(@RequestParam String query) {
        log.info("Querying all states like {}", query);

        return mapToList(stateService.searchStates(query));
    }

    @RequestMapping(method = RequestMethod.GET, params = "identifier")
    public StateWithCitiesDto getStateWithIdentifier(@RequestParam String identifier) {
        log.info("Querying state with identifier {}", identifier);

        return map(stateService.load(identifier).get(), StateWithCitiesDto.class);
    }

    @RequestMapping(value = "/{stateId}", method = RequestMethod.GET)
    public StateWithCitiesDto getState(@PathVariable Long stateId) {
        log.info("Querying state with id {}", stateId);

        return map(stateService.load(stateId), StateWithCitiesDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StateDto> createState(@RequestBody StateDto stateDto) {
        log.info("Creating state with {}", stateDto);

        final State createdState = stateService.createState(stateDto.getName(), stateDto.getIdentifier(),
                stateDto.getImagePath());
        final StateDto createdStateDto = map(createdState);

        return ResponseEntity.created(createdStateDto.uri()).body(createdStateDto);
    }
}
