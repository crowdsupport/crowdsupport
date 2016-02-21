package org.outofrange.crowdsupport.automation.keyword.ui.core;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebDriverWrapper {
    private static final Logger log = LoggerFactory.getLogger(WebDriverWrapper.class);

    private final DriverHandler driverHandler;

    @Inject
    public WebDriverWrapper(DriverHandler driverHandler) {
        this.driverHandler = driverHandler;
    }

    private WebDriver driver() {
        return driverHandler.getDriver();
    }

    private WebElement find(By by) {
        return driver().findElement(by);
    }

    private List<WebElement> findAll(By by) {
        return driver().findElements(by);
    }

    public void setText(By by, CharSequence text) {
        if (text != null) {
            log.trace("Setting text '{}' on element located by {}", text, by);

            final WebElement element = find(by);
            element.clear();
            element.sendKeys(text);
        } else {
            log.trace("text is null, won't set text on element located by {}", by);
        }
    }

    public void sendKeys(By by, CharSequence... keys) {
        log.trace("Sending keys {} to element located by {}", keys, by);

        find(by).sendKeys(keys);
    }

    public void click(By by) {
        log.trace("Clicking element located by {}", by);

        find(by).click();
    }

    public String getText(By by) {
        log.trace("Retrieving text from element located by {}", by);

        return find(by).getText();
    }

    public List<String> getTextFromAll(By by) {
        log.trace("Retrieving all texts from elements located by {}", by);

        return findAll(by).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public <T> Waiter waiter(Function<WebDriver, T> predicate) {
        return Waiter.create(driver(), predicate);
    }

    public boolean isDisplayed(By by) {
        log.trace("Checking if element located by {} is displayed", by);
        try {
            return find(by).isDisplayed();
        } catch (NoSuchElementException e) {
            log.trace("Couldn't find element, therefore it's not displayed");

            return false;
        }
    }

    public boolean isEnabled(By by) {
        log.trace("Checking if element located by {} is enabled", by);

        return find(by).isEnabled();
    }
}
