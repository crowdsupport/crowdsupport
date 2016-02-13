package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DriverHandler {
    private static final Logger log = LoggerFactory.getLogger(DriverHandler.class);

    @Value("${server.port}")
    private String port;

    private WebDriver driver;
    private boolean doReset = true;

    public DriverHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public WebDriver getDriver() {
        if (driver == null) {
            log.info("Creating new WebDriver");
            driver = createFirefoxDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (doReset) {
            log.info("Resetting webdriver - navigating to default URL and maximizing");
            driver.get("http://localhost:" + port);
            driver.manage().window().maximize();

            doReset = false;
        }

        return driver;
    }

    public void resetOnNextUse() {
        log.info("WebDriver will be reset on next use");
        doReset = true;
    }

    public void stop() {
        if (driver != null) {
            log.info("Shutting down WebDriver");
            driver.quit();
            driver = null;
            doReset = true;
        }
    }

    private static WebDriver createFirefoxDriver() {
        return new FirefoxDriver();
    }
}
