package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;

import java.util.Optional;

public interface CityService extends BaseService<City> {
    Optional<City> load(String identifier);
}
