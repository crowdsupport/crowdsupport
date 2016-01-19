package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.RoleDto;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/service/v1/")
public class AuthorityRestService {
    @Inject
    private AuthorityService authorityService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(mapper.mapToList(authorityService.loadAllRoles(), RoleDto.class));
    }
}
