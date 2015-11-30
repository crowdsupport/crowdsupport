package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CityService {
    @Inject
    private CityRepository cityRepository;

    public List<City> getRecentlyUsedCities() {
        final String placeHolder = "/image/placeholder.jpg";
        State uk = new State("United Kingdom");
        State austria = new State("Austria");
        State germany = new State("Germany");

        City preston = new City(uk, "Preston");
        City vienna = new City(austria, "Vienna");
        City salzburg = new City(austria, "Salzburg");
        City hamburg = new City(germany, "Hamburg");
        City london = new City(uk, "London");
        City munich = new City(germany, "Munich");

        final List<City> cities = Arrays.asList(preston, vienna, salzburg, hamburg, london, munich);
        cities.forEach(c -> c.setImagePath(placeHolder));

        Collections.shuffle(cities);

        return cities;
    }
}
