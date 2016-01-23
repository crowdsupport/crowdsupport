package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.ResponseDto;
import org.outofrange.crowdsupport.dto.RoleDto;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/service/v1/")
public class AuthorityRestService {
    private static final Logger log = LoggerFactory.getLogger(AuthorityRestService.class);

    @Inject
    private AuthorityService authorityService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(mapper.mapToList(authorityService.loadAllRoles(), RoleDto.class));
    }

    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<RoleDto>> createRole(@RequestBody String roleName) {
        log.info("Creating new role with name {}", roleName);

        return ResponseEntity.ok(ResponseDto.success("Successfully created role " + roleName,
                mapper.map(authorityService.createRoleIfNeeded(roleName), RoleDto.class)));
    }

    @RequestMapping(value = "/role/{roleName}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteRole(@PathVariable String roleName) {
        log.info("Deleting role {}", roleName);

        authorityService.deleteRole(roleName);

        return ResponseEntity.ok(ResponseDto.success("Successfully deleted role " + roleName));
    }

    @RequestMapping(value = "/role/{roleName}/assignPermissions", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<RoleDto>> changeRolePermissionAssignment(@PathVariable String roleName,
                                                                               @RequestBody List<String> permissions) {
        log.info("Setting new permissions for {}", roleName);

        return ResponseEntity.ok(ResponseDto.success("Successfully changed permission assignment for role " + roleName,
                mapper.map(authorityService.setPermissionsForRole(roleName, permissions), RoleDto.class)));
    }

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllPermissions() {
        return ResponseEntity.ok(mapper.mapToList(authorityService.loadAllPermissions(), String.class));
    }
}
