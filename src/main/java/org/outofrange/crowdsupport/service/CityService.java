package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService extends BaseService<City> {
    Optional<City> load(String identifier);

    List<City> searchCities(String query);

    City saveOrRetrieveCity(City city);
}
