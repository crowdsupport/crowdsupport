package org.outofrange.crowdsupport.rest.v2;

import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Set;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/permission")
public class PermissionRestController extends MappingController {
    private final AuthorityService authorityService;

    @Inject
    public PermissionRestController(CsModelMapper mapper, AuthorityService authorityService) {
        super(mapper);
        this.authorityService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<String>> getAllPermissions() {
        return ResponseEntity.ok(mapToSet(authorityService.loadAllPermissions(), String.class));
    }
}
