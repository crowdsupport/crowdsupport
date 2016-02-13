package org.outofrange.crowdsupport.automation.keyword.ui.core;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class WebDriverWrapper {
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

    public void setText(By by, String text) {
        if (text != null) {
            final WebElement element = find(by);
            element.clear();
            element.sendKeys(text);
        }
    }

    public void click(By by) {
        find(by).click();
    }

    public String getText(By by) {
        return find(by).getText();
    }

    public Waiter waiter(Function<WebDriver, WebElement> predicate) {
        return new Waiter(driver(), predicate);
    }

    public boolean isDisplayed(By by) {
        return find(by).isDisplayed();
    }

    public boolean isEnabled(By by) {
        return find(by).isEnabled();
    }
}
