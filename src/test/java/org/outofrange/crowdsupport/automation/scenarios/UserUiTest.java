package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.outofrange.crowdsupport.automation.keyword.ui.ProfileKeywords;
import org.outofrange.crowdsupport.automation.keyword.ui.SidePanelKeywords;
import org.outofrange.crowdsupport.model.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserUiTest extends UiTest {
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
        sidePanel.login("unknown", "invalid").doWait();

        assertFalse(sidePanel.isLoggedIn());
    }

    @Test
    public void changeProfileSettingsAsUser() {
        final User user = data().user().createUser(false);
        final String newPassword = "newPassword";

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword()).doWait();

        final ProfileKeywords profileKeywords = sidePanel.clickProfile();

        assertFalse(profileKeywords.isUsernameInputEnabled());

        profileKeywords.changeDetails(null, newPassword, null);

        sidePanel.logout();

        sidePanel.login(user.getUsername(), newPassword).doWait();
        assertEquals(user.getUsername(), sidePanel.getLoggedInUsername());

        sidePanel.logout();
        assertTrue(sidePanel.isLoggedOut());
    }

    @Test
    public void changeProfileSettingsAsAdmin() {
        final User user = data().user().createUser(true);
        final String newName = "newName";
        final String newPassword = "newPassword";

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword()).doWait();

        final ProfileKeywords profileKeywords = sidePanel.clickProfile();

        assertTrue(profileKeywords.isUsernameInputEnabled());

        user.setUsername(newName);
        profileKeywords.changeDetails(newName, newPassword, null);

        sidePanel.logout();

        sidePanel.login(newName, newPassword).doWait();
        assertEquals(newName, sidePanel.getLoggedInUsername());

        sidePanel.logout();
        assertTrue(sidePanel.isLoggedOut());
    }

    @Test
    public void cantLoginWithDisabledUser() {
        final User user = data().user().createDisabledUser();

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword()).doWait();

        assertTrue(sidePanel.isLoggedOut());
    }

    private void loginAsSomebody(boolean admin) {
        final User user = data().user().createUser(admin);

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword()).doWait();

        assertEquals(user.getUsername(), sidePanel.getLoggedInUsername());

        sidePanel.logout().doWait();

        assertTrue(sidePanel.isLoggedOut());
    }
}
