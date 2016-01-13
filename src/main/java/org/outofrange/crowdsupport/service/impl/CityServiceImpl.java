package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    private static final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    @Inject
    private CityRepository cityRepository;

    @Override
    public City save(City city) {
        log.debug("Saving city {}", city);

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

    @Override
    public List<City> searchCities(String query) {
        return cityRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    public City saveOrRetrieveCity(City city) {
        final Optional<City> loadedCity = load(city.getIdentifier());

        if (loadedCity.isPresent()) {
            if (loadedCity.get().equals(city)) {
                return loadedCity.get();
            } else {
                throw new ServiceException("Found different city with same identifier: " + loadedCity.get());
            }
        }

        return save(city);
    }
}
