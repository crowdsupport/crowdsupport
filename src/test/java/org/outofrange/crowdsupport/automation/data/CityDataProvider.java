package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.util.Authorized;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

@Component
public class CityDataProvider {
    private static final String DEFAULT_NAME = "City Name";
    private static final String DEFAULT_IDENTIFIER = "cityid";

    private final StateDataProvider stateDataProvider;
    private final CityService cityService;

    @Inject
    public CityDataProvider(StateDataProvider stateDataProvider, CityService cityService) {
        this.stateDataProvider = stateDataProvider;
        this.cityService = cityService;
    }

    public City getCity() {
        final State state = stateDataProvider.getState();

        Optional<City> city = cityService.load(DEFAULT_IDENTIFIER, state.getIdentifier());
        if (city.isPresent()) {
            return city.get();
        } else {
            City newCity = Authorized.asAdmin().run(
                    () -> cityService.createCity(DEFAULT_NAME, DEFAULT_IDENTIFIER, null, state.getIdentifier()));
            DataProvider.registerUndo(() -> cityService.deleteCity(newCity.getId()));

            return newCity;
        }
    }
}
