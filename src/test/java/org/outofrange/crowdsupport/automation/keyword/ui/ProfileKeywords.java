package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class ProfileKeywords extends KeywordBase {
    private static final By USERNAME_INPUT = By.id("profile__username-input");
    private static final By PASSWORD_INPUT = By.id("profile__password-input");
    private static final By EMAIL_INPUT = By.id("profile__email-input");
    private static final By SAVE_BUTTON = By.id("profile__save-button");

    public boolean isUsernameInputEnabled() {
        return web().isEnabled(USERNAME_INPUT);
    }

    public ProfileKeywords changeDetails(String username, String password, String email) {
        web().setText(USERNAME_INPUT, username);
        web().setText(PASSWORD_INPUT, password);
        web().setText(EMAIL_INPUT, email);

        web().click(SAVE_BUTTON);

        return this;
    }

    public String getUsername() {
        return web().getText(USERNAME_INPUT);
    }
}
