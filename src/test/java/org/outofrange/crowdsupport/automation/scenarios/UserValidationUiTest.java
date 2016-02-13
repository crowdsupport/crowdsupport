package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.outofrange.crowdsupport.automation.keyword.ui.SidePanelKeywords;
import org.outofrange.crowdsupport.model.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserValidationUiTest extends UiTest {
    @Test
    public void loginAsAdmin() {
        loginAsSomebody(true);
    }

    @Test
    public void loginAsUser() {
        loginAsSomebody(false);
    }

    @Test
    public void noLoginWithInvalidPassword() {
        final User user = data().user().createUser(false);

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), "Invalid password").doWait();

        assertFalse(sidePanel.isLoggedIn());
    }

    @Test
    public void noLoginWithInvalidUser() {
        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login("Not existing user", "Invalid password").doWait();

        assertFalse(sidePanel.isLoggedIn());
    }

    private void loginAsSomebody(boolean admin) {
        final User user = data().user().createUser(admin);

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword()).doWait();

        assertEquals(user.getUsername(), sidePanel.getLoggedInUsername());

        sidePanel.logout().doWait();

        assertFalse(sidePanel.isLoggedIn());
    }
}
