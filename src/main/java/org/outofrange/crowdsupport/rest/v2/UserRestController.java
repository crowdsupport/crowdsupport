package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/user")
public class UserRestController extends TypeMappingController<UserDto> {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    @Inject
    public UserRestController(ModelMapper mapper, UserService userService) {
        super(mapper, UserDto.class);
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, params = "!query")
    public List<UserDto> getAllUsers() {
        log.info("Querying all users");

        return mapToList(userService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    public List<UserDto> getAllUsersLike(@RequestParam String query) {
        log.info("Querying all users like {}", query);

        return mapToList(userService.queryUsers(query));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Querying user with id {}", userId);

        return map(userService.loadUser(userId));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public FullUserDto changeUserDetails(@PathVariable Long userId, @RequestBody FullUserDto userDto) {
        log.info("Changing details of user with id {}", userId);

        return getMapper().map(userService.updateAll(userId, userDto), FullUserDto.class);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<FullUserDto> createUser(@RequestBody FullUserDto userDto) {
        log.info("Creating new user with dto {}", userDto);

        final FullUserDto createdUser = map(userService.createUser(userDto), FullUserDto.class);

        return ResponseEntity.created(createdUser.uri()).body(createdUser);
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public FullUserDto getCurrentUser() {
        log.info("Querying logged in user");

        return map(userService.getCurrentUserUpdated(), FullUserDto.class);
    }

    @RequestMapping(value = "/current", method = RequestMethod.PATCH)
    public FullUserDto changeOwnUserDetails(FullUserDto userDto) {
        log.info("Changing details for logged in user");

        return map(userService.updateProfile(userDto), FullUserDto.class);
    }
}
