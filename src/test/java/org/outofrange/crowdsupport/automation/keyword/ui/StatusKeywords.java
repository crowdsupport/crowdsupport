package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.outofrange.crowdsupport.automation.keyword.ui.core.CustomConditions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusKeywords extends KeywordBase {
    private static final By MESSAGES = By.className("status-message");
    private static final By LASTMESSAGE = By.cssSelector(".status-message:last-of-type");

    public List<String> getMessages() {
        return web().getTextFromAll(MESSAGES);
    }

    public String getMessage() {
        web().waiter(CustomConditions.anyTextInElement(LASTMESSAGE)).doWait(10);
        return web().getText(LASTMESSAGE);
    }

    public boolean isMessageDisplayed(String containingText) {
        web().waiter(ExpectedConditions.textToBePresentInElementLocated(LASTMESSAGE, containingText)).doWait();
        return web().getText(LASTMESSAGE).contains(containingText);
    }
}
