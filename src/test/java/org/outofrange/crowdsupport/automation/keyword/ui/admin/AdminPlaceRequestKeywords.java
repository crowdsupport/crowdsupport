package org.outofrange.crowdsupport.automation.keyword.ui.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.outofrange.crowdsupport.automation.keyword.ui.KeywordBase;
import org.outofrange.crowdsupport.automation.keyword.ui.core.Sleeper;
import org.springframework.stereotype.Component;

@Component
public class AdminPlaceRequestKeywords extends KeywordBase {
    public RequestedPlace forPlaceIdentifier(String placeIdentifier) {
        return new RequestedPlace(placeIdentifier);
    }

    public class RequestedPlace {
        private final String placeIdentifier;

        private RequestedPlace(String placeIdentifier) {
            this.placeIdentifier = placeIdentifier;
        }

        private By byForPlace(String cssSelector) {
            String selector = ".requested-place." + placeIdentifier;
            if (cssSelector != null) {
                selector += " " + cssSelector;
            }

            return By.cssSelector(selector);
        }

        // @formatter:off
        private By nameInput() { return byForPlace(".name-input"); }
        private By identifierInput() { return byForPlace(".identifier-input"); }
        private By locationInput() { return byForPlace(".location-input"); }

        private By cityAutocomplete() { return byForPlace(".city-autocomplete input"); }
        private By toCityInputButton() { return byForPlace(".to-city-input-button"); }
        private By toCitySearchButton() { return byForPlace(".to-city-search-button"); }
        private By cityNameInput() { return byForPlace(".city-name-input"); }
        private By cityIdentifierInput() { return byForPlace(".city-identifier-input"); }
        private By createCityButton() { return byForPlace(".create-city-button"); }

        private By stateAutocomplete() { return byForPlace(".state-autocomplete input"); }
        private By toStateInputButton() { return byForPlace(".to-state-input-button"); }
        private By toStateSearchButton() { return byForPlace(".to-state-search-button"); }
        private By stateNameInput() { return byForPlace(".state-name-input"); }
        private By stateIdentifierInput() { return byForPlace(".state-identifier-input"); }
        private By createStateButton() { return byForPlace(".create-state-button"); }

        private By declineButton() { return byForPlace(".decline-button"); }
        private By acceptButton() { return byForPlace(".accept-button"); }
        // @formatter:on


        public RequestedPlace setName(String name) {
            web().setText(nameInput(), name);
            return this;
        }

        public RequestedPlace setIdentifier(String identifier) {
            web().setText(identifierInput(), identifier);
            return this;
        }

        public RequestedPlace setLocation(String location) {
            web().setText(locationInput(), location);
            return this;
        }

        public RequestedPlace setCityAutocomplete(String citySearch) {
            web().setText(cityAutocomplete(), citySearch);
            return this;
        }

        public RequestedPlace setCityName(String cityName) {
            web().setText(cityNameInput(), cityName);
            return this;
        }

        public RequestedPlace setCityIdentifier(String cityIdentifier) {
            web().setText(cityIdentifierInput(), cityIdentifier);
            return this;
        }

        public RequestedPlace clickToManualCityInput() {
            web().click(toCityInputButton());
            return this;
        }

        public RequestedPlace clickToCitySearch() {
            web().click(toCitySearchButton());
            return this;
        }

        public RequestedPlace createCity() {
            web().click(createCityButton());
            return this;
        }

        public boolean isCreateCityButtonEnabled() {
            return web().isEnabled(createCityButton());
        }

        public RequestedPlace setStateAutocomplete(String stateSearch) {
            web().setText(stateAutocomplete(), stateSearch);
            Sleeper.sleep(1000);
            web().sendKeys(stateAutocomplete(), Keys.ENTER);
            return this;
        }

        public RequestedPlace setStateName(String stateName) {
            web().setText(stateNameInput(), stateName);
            return this;
        }

        public RequestedPlace setStateIdentifier(String stateIdentifier) {
            web().setText(stateIdentifierInput(), stateIdentifier);
            return this;
        }

        public RequestedPlace clickToManualStateInput() {
            web().click(toStateInputButton());
            return this;
        }

        public RequestedPlace clickToStateSearch() {
            web().click(toStateSearchButton());
            return this;
        }

        public RequestedPlace createState() {
            web().click(createStateButton());
            return this;
        }

        public void declineRequest() {
            web().click(declineButton());
        }

        public void acceptRequest() {
            web().click(acceptButton());
        }

        public boolean isDisplayed() {
            return web().isDisplayed(byForPlace(null));
        }
    }
}
