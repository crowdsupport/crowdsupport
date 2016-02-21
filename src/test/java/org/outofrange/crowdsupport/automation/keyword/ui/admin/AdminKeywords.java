package org.outofrange.crowdsupport.automation.keyword.ui.admin;

import org.openqa.selenium.By;
import org.outofrange.crowdsupport.automation.keyword.ui.KeywordBase;
import org.springframework.stereotype.Component;

@Component
public class AdminKeywords extends KeywordBase {
    private static final By REQUESTED_PLACES_BUTTON = By.id("admin__requested-places");
    private static final By USER_MANAGEMENT_BUTTON = By.id("admin__user-management");
    private static final By ROLE_MANAGEMENT_BUTTON = By.id("admin__role-management");
    private static final By SETTINGS_BUTTON = By.id("admin__settings");

    public AdminPlaceRequestKeywords gotoRequestedPlaces() {
        web().click(REQUESTED_PLACES_BUTTON);

        return getKeywords(AdminPlaceRequestKeywords.class);
    }
}
