package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class DashboardKeywords extends KeywordBase {
    private static final By BROWSE_STATES_BUTTON = By.id("dashboard__browse-states-button");

    public StatesKeywords browseStates() {
        web().click(BROWSE_STATES_BUTTON);
        return getKeywords(StatesKeywords.class);
    }
}
