package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

@RequestMapping(value = "/service/v1/user")
@RestController
public class UserRestService {
    @Inject
    private UserService userService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<UserDto> getCurrentUser() {
        final Optional<User> currentUser = userService.getCurrentUserUpdated();

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(mapper.map(currentUser.get(), UserDto.class));
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
