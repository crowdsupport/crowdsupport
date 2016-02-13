package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class KeywordBase {
    @Inject
    private DriverHandler driverHandler;

    protected WebDriver driver() {
        return driverHandler.getDriver();
    }
}
