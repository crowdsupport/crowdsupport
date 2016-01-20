package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.ResponseDto;
import org.outofrange.crowdsupport.dto.CurrentUserDto;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/service/v1/user")
@RestController
public class UserRestService {
    private static final Logger log = LoggerFactory.getLogger(UserRestService.class);

    @Inject
    private UserService userService;

    @Inject
    private CsModelMapper mapper;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public ResponseEntity<CurrentUserDto> getCurrentUser() {
        final Optional<User> currentUser = userService.getCurrentUserUpdated();

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(mapper.map(currentUser.get(), CurrentUserDto.class));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CurrentUserDto>> getUsers(@RequestParam(required = false) String query) {
        final List<User> users;
        if (query == null) {
            log.info("Loading all users");
            users = userService.loadAll();
        } else {
            log.info("Loading users like {}", query);
            users = userService.queryUsers(query);
        }

        return ResponseEntity.ok(mapper.mapToList(users, CurrentUserDto.class));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseDto<CurrentUserDto>> createUser(@RequestBody CurrentUserDto userDto) {
        log.info("Register user: {}", userDto);

        if (userService.isUserLoggedIn()) {
            return ResponseEntity.badRequest().body(ResponseDto.error("Registration not allowed when logged in", null));
        }

        final User user = mapper.map(userDto, User.class);
        return ResponseEntity.ok(ResponseDto.success("Registration was successful", mapper.map(userService.save(user), CurrentUserDto.class)));
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseDto<CurrentUserDto>> updateUser(@RequestBody CurrentUserDto userDto, @PathVariable String username,
                                                                  @RequestParam(required = false) boolean all) {
        log.info("Updating user: {}",  username);

        if (Boolean.TRUE.equals(all)) {
            final CurrentUserDto savedUserDto = mapper.map(userService.updateAll(username, userDto), CurrentUserDto.class);
            return ResponseEntity.ok(ResponseDto.success("Data for user " + username + " changed successfully", savedUserDto));
        } else {
            final CurrentUserDto savedUserDto = mapper.map(userService.updateProfile(userDto), CurrentUserDto.class);
            return ResponseEntity.ok(ResponseDto.success("Profile data changed successfully", savedUserDto));
        }
    }
}
