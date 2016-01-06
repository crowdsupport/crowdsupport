package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.rest.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/service/v1/user")
public class UserRestService {
    @Inject
    private ModelMapper mapper;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public UserDto getUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return mapper.map(principal, UserDto.class);
        } else {
            return null;
        }
    }
}
