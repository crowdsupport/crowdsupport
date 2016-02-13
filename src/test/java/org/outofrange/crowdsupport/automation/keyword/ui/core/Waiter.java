package org.outofrange.crowdsupport.automation.keyword.ui.core;

import com.google.common.base.Function;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Waiter<R> {
    private static final Logger log = LoggerFactory.getLogger(Waiter.class);

    private static final int WAIT_DEFAULT = 5;

    private final WebDriver webDriver;
    private final Function<WebDriver, R> predicate;

    private Waiter(WebDriver webDriver, Function<WebDriver, R> predicate) {
        this.webDriver = webDriver;
        this.predicate = predicate;
    }

    protected static <V> Waiter<V> create(WebDriver webDriver, Function<WebDriver, V> predicate) {
        return new Waiter<>(webDriver, predicate);
    }

    public R doWait() {
        return doWait(WAIT_DEFAULT);
    }

    public R doWait(int seconds) {
        try {
            return new WebDriverWait(webDriver, seconds).until(predicate);
        } catch (TimeoutException e) {
            log.info("Waiting timed out after {} seconds - {}", seconds, e.getMessage());

            return null;
        }
    }
}
