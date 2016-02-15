package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class CityKeywords extends KeywordBase {
    private static By GOTO_PLACE_BUTTON(String identifier) {
        return By.cssSelector("#places__" + identifier + " .places__place-button");
    }

    public PlaceKeywords gotoPlace(String placeIdentifier) {
        web().click(GOTO_PLACE_BUTTON(placeIdentifier));

        return getKeywords(PlaceKeywords.class);
    }
}
