package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService extends BaseService<City> {
    Optional<City> load(String identifier, String stateIdentifier);

    City load(long id);

    List<City> searchCities(String query);

    City saveOrRetrieveCity(City city);

    City createCity(String name, String identifier, String imagePath, String stateIdentifier);
}
