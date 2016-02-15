package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.Authorized;
import org.outofrange.crowdsupport.util.TestUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserDataProvider {
    private final UserService userService;

    @Inject
    public UserDataProvider(UserService userService) {
        this.userService = userService;
    }

    public User createUser(boolean admin) {
        final TestUser user = admin ? TestUser.ADMIN : TestUser.USER;

        final User created = userService.createUser(user.getFullUserDto());
        // we want our raw password so we can login
        created.setPassword(user.getFullUserDto().getPassword());

        if (admin) {
            // we have to set roles as an explicit update call
            Authorized.asAdmin().run(() ->  userService.makeAdmin(created.getId()));
        }

        DataProvider.registerUndo(() -> userService.disableUser(created.getId()));

        return created;
    }

    public User createDisabledUser() {
        final User created = userService.createUser(TestUser.USER.getFullUserDto());
        userService.disableUser(created.getId());

        return created;
    }
}
