package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.util.Authorized;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

@Component
public class PlaceDataProvider {
    private static final String DEFAULT_NAME = "City Name";
    private static final String DEFAULT_IDENTIFIER = "cityid";
    private static final String DEFAULT_LOCATION = "Trainstation";

    private final CityDataProvider cityDataProvider;
    private final PlaceService placeService;

    @Inject
    public PlaceDataProvider(CityDataProvider cityDataProvider, PlaceService placeService) {
        this.cityDataProvider = cityDataProvider;
        this.placeService = placeService;
    }

    public Place getPlace() {
        final City city = cityDataProvider.getCity();

        Optional<Place> place = placeService.load(city.getState().getIdentifier(), city.getIdentifier(), DEFAULT_IDENTIFIER);
        if (place.isPresent()) {
            return place.get();
        } else {
            Place newPlace = Authorized.asAdmin().run(
                    () -> placeService.save(new Place(city, DEFAULT_NAME, DEFAULT_IDENTIFIER, DEFAULT_LOCATION)));
            DataProvider.registerUndo(() -> placeService.deletePlace(newPlace.getId()));

            return newPlace;
        }
    }
}
