package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Set;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/permission")
public class PermissionRestController extends MappingController {
    private static final Logger log = LoggerFactory.getLogger(PermissionRestController.class);

    private final AuthorityService authorityService;

    @Inject
    public PermissionRestController(ModelMapper mapper, AuthorityService authorityService) {
        super(mapper);
        this.authorityService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<String>> getAllPermissions() {
        log.info("Querying all permissions");

        return ResponseEntity.ok(mapToSet(authorityService.loadAllPermissions(), String.class));
    }
}
