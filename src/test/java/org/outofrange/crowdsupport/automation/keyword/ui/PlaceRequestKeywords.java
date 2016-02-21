package org.outofrange.crowdsupport.automation.keyword.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.outofrange.crowdsupport.automation.keyword.ui.core.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlaceRequestKeywords extends KeywordBase {
    private static final Logger log = LoggerFactory.getLogger(PlaceRequestKeywords.class);

    private static final By NAME_INPUT = By.id("placerequest__name-input");
    private static final By IDENTIFIER_INPUT = By.id("placerequest__identifier-input");
    private static final By CITY_AUTOCOMPLETE = By.id("placerequest__city-autocomplete");
    private static final By TO_CITY_INPUT_BUTTON = By.id("placerequest__to-city-input-button");
    private static final By CITY_INPUT = By.id("placerequest__city-input");
    private static final By STATE_INPUT = By.id("placerequest__state-input");
    private static final By TO_CITY_SEARCH_BUTTON = By.id("placerequest__to-city-search-button");
    private static final By LOCATION_INPUT = By.id("placerequest__location-input");
    private static final By REQUEST_BUTTON = By.id("placerequest__request-button");

    public PlaceRequestKeywords setName(String name) {
        web().setText(NAME_INPUT, name);

        return this;
    }

    public PlaceRequestKeywords setIdentifier(String identifier) {
        web().setText(IDENTIFIER_INPUT, identifier);

        return this;
    }

    public PlaceRequestKeywords setCityAutocomplete(String cityAutocomplete) {
        web().setText(CITY_AUTOCOMPLETE, cityAutocomplete);
        Sleeper.sleep(500);
        web().sendKeys(CITY_AUTOCOMPLETE, Keys.ENTER);
        return this;
    }

    public PlaceRequestKeywords clickToCityInputButton() {
        web().click(TO_CITY_INPUT_BUTTON);

        return this;
    }

    public PlaceRequestKeywords setCityName(String cityName) {
        web().setText(CITY_INPUT, cityName);

        return this;
    }

    public PlaceRequestKeywords setStateName(String stateName) {
        web().setText(STATE_INPUT, stateName);

        return this;
    }

    public PlaceRequestKeywords clickToCitySearchButton() {
        web().click(TO_CITY_SEARCH_BUTTON);

        return this;
    }

    public PlaceRequestKeywords setLocation(String location) {
        web().setText(LOCATION_INPUT, location);

        return this;
    }

    public void clickRequestButton() {
        try {
            web().click(REQUEST_BUTTON);
        } catch (WebDriverException e) {
            log.warn("Clicking again - most likely we had md-scroll-mask annoying us, should be gone after the first click");
            web().click(REQUEST_BUTTON);
        }
    }
}
