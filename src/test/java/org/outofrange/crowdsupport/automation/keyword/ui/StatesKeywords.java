package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class StatesKeywords extends KeywordBase {
    private static By GOTO_CITIES_BUTTON(String stateIdentifier) {
        return By.cssSelector("#states__" + stateIdentifier + " .states__cities-button");
    }

    public StateKeywords gotoCities(String stateIdentifier) {
        web().click(GOTO_CITIES_BUTTON(stateIdentifier));

        return getKeywords(StateKeywords.class);
    }
}
