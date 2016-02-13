package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.outofrange.crowdsupport.automation.keyword.ui.core.Waiter;
import org.springframework.stereotype.Component;

@Component
public class RegisterKeywords extends KeywordBase {
    private static final By USERNAME_INPUT = By.id("register__username-input");
    private static final By PASSWORD_INPUT = By.id("register__password-input");
    private static final By EMAIL_INPUT = By.id("register__email-input");
    private static final By REGISTER_BUTTON = By.id("register__register-button");

    public RegisterKeywords enterDetails(String username, String password, String email) {
        web().setText(USERNAME_INPUT, username);
        web().setText(PASSWORD_INPUT, password);
        web().setText(EMAIL_INPUT, email);

        return this;
    }

    public Waiter clickRegister() {
        web().click(REGISTER_BUTTON);

        return web().waiter(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(REGISTER_BUTTON)));
    }
}
