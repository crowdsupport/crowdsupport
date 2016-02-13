package org.outofrange.crowdsupport.automation.keyword.ui.core;

import com.google.common.base.Function;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Waiter {
    private static final Logger log = LoggerFactory.getLogger(Waiter.class);

    private static final int WAIT_DEFAULT = 5;

    private final WebDriver webDriver;
    private final Function<WebDriver, WebElement> predicate;

    protected Waiter(WebDriver webDriver, Function<WebDriver, WebElement> predicate) {
        this.webDriver = webDriver;
        this.predicate = predicate;
    }

    public void doWait() {
        doWait(WAIT_DEFAULT);
    }

    public void doWait(int seconds) {
        try {
            new WebDriverWait(webDriver, seconds).until(predicate);
        } catch (TimeoutException e) {
            log.info("Waiting timed out after {} seconds - {}", seconds, e.getMessage());
        }
    }
}
