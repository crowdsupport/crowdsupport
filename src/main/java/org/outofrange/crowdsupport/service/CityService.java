package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    @Inject
    private CityRepository cityRepository;

    public City save(City city) {
        return cityRepository.save(city);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
