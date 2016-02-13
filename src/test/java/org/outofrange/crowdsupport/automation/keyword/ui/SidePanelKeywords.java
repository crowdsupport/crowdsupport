package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.outofrange.crowdsupport.automation.keyword.Waiter;
import org.springframework.stereotype.Component;

@Component
public class SidePanelKeywords extends KeywordBase {
    private static final By LOGIN_INPUT = By.id("sidepanel__login-input");
    private static final By PASSWORD_INPUT = By.id("sidepanel__password-input");
    private static final By LOGIN_BUTTON = By.id("sidepanel__login-button");
    private static final By LOGOUT_BUTTON = By.id("sidepanel__logout-button");
    private static final By REGISTER_BUTTON = By.id("sidepanel__register-button");
    private static final By LOGGEDIN_USER_TEXT = By.id("sidepanel__loggedin-name");

    public boolean isLoggedIn() {
        return driver().findElement(LOGGEDIN_USER_TEXT).isDisplayed();
    }

    public Waiter login(String username, String password) {
        driver().findElement(LOGIN_INPUT).sendKeys(username);
        driver().findElement(PASSWORD_INPUT).sendKeys(password);
        driver().findElement(LOGIN_BUTTON).click();

        return new Waiter(driver(), ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
    }

    public Waiter logout() {
        driver().findElement(LOGOUT_BUTTON).click();

        return new Waiter(driver(), ExpectedConditions.visibilityOfElementLocated(LOGIN_BUTTON));
    }

    public String getLoggedInUsername() {
        return driver().findElement(LOGGEDIN_USER_TEXT).getText();
    }
}
