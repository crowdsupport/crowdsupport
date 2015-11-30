package org.outofrange.crowdsupport.util;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.service.StateService;
import org.outofrange.crowdsupport.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.GeoResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TestDataService {
    private static final Logger log = LoggerFactory.getLogger(TestDataService.class);

    @Inject
    private StateService stateService;

    @Inject
    private CityService cityService;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        log.info("Creating test data");

        createUsers();
        createStatesAndCities();
    }

    private void createUsers() {
        log.info("Create users");
        User admin = new User("admin", "admin");
        admin.setAdmin(true);
        User normal = new User("user", "user");
        userService.registerNewUser(admin);
        userService.registerNewUser(normal);
    }

    private void createStatesAndCities() {
        final String placeHolder = "/image/placeholder.jpg";
        State uk = new State("United Kingdom");
        State austria = new State("Austria");
        State germany = new State("Germany");
        State hungary = new State("Hungary");
        State greece = new State("Greece");

        final List<State> states = Arrays.asList(uk, austria, germany, hungary, greece);
        states.forEach(s -> stateService.save(s));

        City preston = new City(uk, "Preston");
        City vienna = new City(austria, "Vienna");
        City salzburg = new City(austria, "Salzburg");
        City hamburg = new City(germany, "Hamburg");
        City london = new City(uk, "London");
        City munich = new City(germany, "Munich");
        City budapest = new City(hungary, "Budapest");
        City kiel = new City(germany, "Kiel");
        City frankfurt = new City(germany, "Frankfurt");
        City spielfeld = new City(austria, "Spielfeld");

        final List<City> cities = Arrays.asList(preston, vienna, salzburg, hamburg, london, munich, budapest, kiel,
                frankfurt, spielfeld);
        cities.forEach(c -> c.setImagePath(placeHolder));

        cities.forEach(c -> cityService.save(c));
    }
}
