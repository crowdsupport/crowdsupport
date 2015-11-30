package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.service.CityService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    @Inject
    private CityRepository cityRepository;

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public List<City> loadAll() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<City> load(String identifier) {
        return cityRepository.findOneByIdentifier(identifier);
    }
}
