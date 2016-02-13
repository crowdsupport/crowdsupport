package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DriverHandler {
    @Value("${server.port}")
    private String port;

    private WebDriver driver;
    private boolean running = false;

    public WebDriver getDriver() {
        if (driver == null) {
            driver = createFirefoxDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (!running) {
            driver.get("http://localhost:" + port);
            driver.manage().window().maximize();

            running = true;
        }

        return driver;
    }

    public void stop() {
        if (driver != null && running) {
            driver.quit();
            driver = null;
            running = false;
        }
    }

    private static WebDriver createFirefoxDriver() {
        return new FirefoxDriver();
    }

    private static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver.exe");
        return new ChromeDriver();
    }
}
