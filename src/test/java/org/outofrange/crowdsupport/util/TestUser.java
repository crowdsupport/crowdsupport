package org.outofrange.crowdsupport.util;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.dto.UserAuthDto;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;

public enum TestUser {
    ADMIN("testadmin", "admin", RoleStore.ADMIN, RoleStore.USER),
    USER("testuser", "user", RoleStore.USER);

    private final UserAuthDto userAuthDto;
    private final FullUserDto fullUserDto;

    TestUser(String username, String password, String... roles) {
        userAuthDto = new UserAuthDto();
        userAuthDto.setUsername(username);
        userAuthDto.setPassword(password);
        userAuthDto.setExp(ZonedDateTime.now().plusDays(1).toEpochSecond());

        fullUserDto = new FullUserDto();
        fullUserDto.setUsername(username);
        fullUserDto.setPassword(password);
        fullUserDto.setRoles(new HashSet<>(Arrays.asList(roles)));
    }

    public UserAuthDto getUserAuthDto() {
        return userAuthDto;
    }

    public FullUserDto getFullUserDto() {
        return fullUserDto;
    }
}
