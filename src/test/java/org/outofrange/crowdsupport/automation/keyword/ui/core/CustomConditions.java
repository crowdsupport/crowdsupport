package org.outofrange.crowdsupport.automation.keyword.ui.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomConditions {
    private static final Logger log = LoggerFactory.getLogger(CustomConditions.class);

    private CustomConditions() { /* no instantiation */ }

    public static ExpectedCondition<Boolean> anyTextInElement(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    final String elementText = findElement(locator, driver).getText();
                    log.trace("Waiting for any text in element. Text is: {}", elementText);
                    return !elementText.equals("");
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "any text to be present in element found by " + locator;
            }
        };
    }

    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (WebDriverException e) {
            log.warn("WebDriverException thrown by findElement({})", by, e);
            throw e;
        }
    }
}
