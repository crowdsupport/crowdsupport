package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.outofrange.crowdsupport.automation.keyword.ui.SidePanelKeywords;
import org.outofrange.crowdsupport.model.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginLogout extends UiTest {
    @Test
    public void loginAsAdmin() {
        loginAsSomebody(true);
    }

    @Test
    public void loginAsUser() {
        loginAsSomebody(false);
    }

    private void loginAsSomebody(boolean admin) {
        final User user = data().user().createUser(admin);

        final SidePanelKeywords sidePanel = keywords().ui().sidePanel();
        sidePanel.login(user.getUsername(), user.getPassword());

        assertEquals(user.getUsername(), sidePanel.getLoggedInUsername());
    }
}
