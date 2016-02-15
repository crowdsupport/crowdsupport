package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class StateKeywords extends KeywordBase {
    private static By GOTO_PLACES_BUTTON(String cityIdentifier) {
        return By.cssSelector("#cities__" + cityIdentifier + " .cities__places-button");
    }

    public CityKeywords gotoPlaces(String cityIdentifier) {
        web().click(GOTO_PLACES_BUTTON(cityIdentifier));

        return getKeywords(CityKeywords.class);
    }
}
