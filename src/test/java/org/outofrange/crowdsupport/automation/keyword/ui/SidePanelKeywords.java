package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.outofrange.crowdsupport.automation.keyword.ui.core.Waiter;
import org.springframework.stereotype.Component;

@Component
public class SidePanelKeywords extends KeywordBase {
    private static final By LOGIN_INPUT = By.id("sidepanel__login-input");
    private static final By PASSWORD_INPUT = By.id("sidepanel__password-input");
    private static final By LOGIN_BUTTON = By.id("sidepanel__login-button");
    private static final By LOGOUT_BUTTON = By.id("sidepanel__logout-button");
    private static final By REGISTER_BUTTON = By.id("sidepanel__register-button");
    private static final By PROFILE_BUTTON = By.id("sidepanel__profile-button");

    private static final By LOGGEDIN_USER_TEXT = By.id("sidepanel__loggedin-name");

    public boolean isLoggedIn() {
        return web().isDisplayed(LOGGEDIN_USER_TEXT);
    }

    public boolean isLoggedOut() {
        return web().isDisplayed(LOGIN_BUTTON);
    }

    public Waiter login(String username, String password) {
        web().setText(LOGIN_INPUT, username);
        web().setText(PASSWORD_INPUT, password);

        web().click(LOGIN_BUTTON);

        return web().waiter(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
    }

    public void logout() {
        web().click(LOGOUT_BUTTON);
    }

    public String getLoggedInUsername() {
        return web().getText(LOGGEDIN_USER_TEXT);
    }

    public ProfileKeywords gotoProfile() {
        web().click(PROFILE_BUTTON);

        return getKeywords(ProfileKeywords.class);
    }

    public RegisterKeywords gotoRegister() {
        web().click(REGISTER_BUTTON);

        return getKeywords(RegisterKeywords.class);
    }
}
