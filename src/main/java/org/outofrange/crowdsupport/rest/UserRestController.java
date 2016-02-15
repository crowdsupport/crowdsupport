package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.dto.UserDto;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.spring.api.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/user")
public class UserRestController extends TypedMappingController<UserDto> {
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
    public List<FullUserDto> getAllUsersLike(@RequestParam String query) {
        log.info("Querying all users like {}", query);

        return mapToList(userService.queryUsers(query), FullUserDto.class);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Querying user with id {}", userId);

        return map(userService.loadUser(userId));
    }

    @RequestMapping(value = "/confirmMail", method = RequestMethod.POST)
    public ResponseEntity<Void> confirmUserMail(@RequestBody String confirmationId) {
        log.info("Confirming user email with id {}", confirmationId);

        userService.confirmEmail(confirmationId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public FullUserDto changeUserDetails(@PathVariable Long userId, @RequestBody FullUserDto userDto) {
        log.info("Changing details of user with id {}", userId);

        return getMapper().map(userService.updateAll(userId, userDto), FullUserDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FullUserDto> createUser(@RequestBody FullUserDto userDto) {
        log.info("Creating new user with dto {}", userDto);

        final FullUserDto createdUser = map(userService.createUser(userDto), FullUserDto.class);

        return ResponseEntity.created(createdUser.uri()).body(createdUser);
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public ResponseEntity<FullUserDto> getCurrentUser() {
        final Optional<User> currentUser = userService.getCurrentUserUpdated();

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(map(currentUser.get(), FullUserDto.class));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping(value = "/current", method = RequestMethod.PATCH)
    public FullUserDto changeOwnUserDetails(@RequestBody FullUserDto userDto) {
        log.info("Changing details for logged in user");

        return map(userService.updateProfile(userDto), FullUserDto.class);
    }
}
