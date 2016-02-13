package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.automation.ActionStack;
import org.outofrange.crowdsupport.automation.Cleanable;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.TestUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserDataProvider implements Cleanable {
    private final UserService userService;
    private final ActionStack undoActions = new ActionStack();

    @Inject
    public UserDataProvider(UserService userService) {
        this.userService = userService;
    }

    public User createUser(boolean admin) {
        final TestUser user = admin ? TestUser.ADMIN : TestUser.USER;

        final User created = userService.createUser(user.getFullUserDto());
        // we want our raw password so we can login
        created.setPassword(user.getFullUserDto().getPassword());

        undoActions.addAction(() -> userService.disableUser(created.getId()));

        return created;
    }

    @Override
    public void cleanUp() {
        undoActions.executeAll();
    }
}
