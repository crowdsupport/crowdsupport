package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.ResponseDto;
import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

@RequestMapping(value = "/service/v1/user")
@RestController
public class UserRestService {
    private static final Logger log = LoggerFactory.getLogger(UserRestService.class);

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<UserDto>> saveUserDetails(@RequestBody UserDto userDto) {
        log.info("Updating user: {}",  userDto);

        final User user = userService.getCurrentUserUpdated().get();

        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !"".equals(userDto.getPassword())) {
            user.setPassword(userDto.getPassword());
        }

        final UserDto savedUserDto = mapper.map(userService.save(user), UserDto.class);
        return ResponseEntity.ok(ResponseDto.success("Profile data changed successfully", savedUserDto));
    }
}
