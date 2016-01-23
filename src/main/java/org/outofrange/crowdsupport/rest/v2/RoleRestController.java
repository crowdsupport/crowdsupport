package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.RoleDto;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Set;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/role")
public class RoleRestController extends TypeMappingController<RoleDto> {
    private static final Logger log = LoggerFactory.getLogger(RoleRestController.class);

    private final AuthorityService authorityService;

    @Inject
    public RoleRestController(ModelMapper mapper, AuthorityService authorityService) {
        super(mapper, RoleDto.class);
        this.authorityService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<RoleDto> getAllRoles() {
        log.info("Querying all roles");

        return mapToSet(authorityService.loadAllRoles(), RoleDto.class);
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.GET)
    public RoleDto getRole(@PathVariable String roleName) {
        log.info("Querying role with name {}", roleName);

        return map(authorityService.loadRole(roleName));
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.PUT)
    public ResponseEntity<RoleDto> addNewRole(@PathVariable String roleName) {
        log.info("Adding new role {}", roleName);

        final RoleDto role = map(authorityService.createRoleIfNeeded(roleName));

        return ResponseEntity.created(role.uri()).body(role);
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRole(@PathVariable String roleName) {
        log.info("Deleting role {}", roleName);

        authorityService.deleteRole(roleName);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{roleName}/permissions/{permission}", method = RequestMethod.PUT)
    public RoleDto addPermissionToRole(@PathVariable String roleName, @PathVariable String permission) {
        log.info("Adding permission {} to role with name {}", permission, roleName);

        return map(authorityService.addPermissionToRole(roleName, permission));
    }

    @RequestMapping(value = "/{roleName}/permissions/{permission}", method = RequestMethod.DELETE)
    public RoleDto removePermissionToRole(@PathVariable String roleName, @PathVariable String permission) {
        log.info("Removing permission {} from role with name {}", permission, roleName);

        return map(authorityService.removePermissionFromRole(roleName, permission));
    }
}
