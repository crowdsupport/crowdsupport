package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

@Component
public class PlaceKeywords extends KeywordBase {
    private static final By SPEED_DIAL_FAB = By.id("place__speeddial");
    private static final By SPEED_DIAL_ADD_REQUEST_FAB = By.id("place__speeddial-add-request");
    private static final By NEW_REQUEST_TITLE_INPUT = By.id("new-request-title");
    private static final By NEW_REQUEST_DESCRIPTION_INPUT = By.id("new-request-description");
    private static final By NEW_REQUEST_QUANTITY_INPUT = By.id("new-request-quantity");
    private static final By NEW_REQUEST_UNITS_INPUT = By.id("new-request-units");
    private static final By NEW_REQUEST_CREATE_BUTTON = By.id("new-request-create");

    public void addDonationRequest(String title, String description, String quantity, String units) {
        web().click(SPEED_DIAL_FAB);
        web().waiter(ExpectedConditions.visibilityOfAllElementsLocatedBy(SPEED_DIAL_ADD_REQUEST_FAB)).doWait();
        web().click(SPEED_DIAL_ADD_REQUEST_FAB);
        web().waiter(ExpectedConditions.visibilityOfAllElementsLocatedBy(NEW_REQUEST_TITLE_INPUT)).doWait();

        web().setText(NEW_REQUEST_TITLE_INPUT, title);
        web().setText(NEW_REQUEST_DESCRIPTION_INPUT, description);
        web().setText(NEW_REQUEST_QUANTITY_INPUT, quantity);
        web().setText(NEW_REQUEST_UNITS_INPUT, units);

        web().click(NEW_REQUEST_CREATE_BUTTON);
    }
}
