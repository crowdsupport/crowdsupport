package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.service.StateService;
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

    @Inject
    private StateService stateService;

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
    public Optional<City> load(String identifier, String stateIdentifier) {
        return cityRepository.findOneByStateIdentifierAndIdentifier(stateIdentifier, identifier);
    }

    @Override
    public List<City> searchCities(String query) {
        return cityRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    public City saveOrRetrieveCity(City city) {
        final Optional<City> loadedCity = load(city.getIdentifier(), city.getState().getIdentifier());

        if (loadedCity.isPresent()) {
            if (loadedCity.get().equals(city)) {
                return loadedCity.get();
            } else {
                throw new ServiceException("Found different city with same identifier: " + loadedCity.get());
            }
        }

        return save(city);
    }

    @Override
    public City createCity(String name, String identifier, String imagePath, String stateIdentifier) {
        if (cityRepository.findOneByStateIdentifierAndIdentifier(stateIdentifier, identifier).isPresent()) {
            throw new ServiceException("There is already a city with an identifier of " + identifier +
                    " in a state with the identifier " + stateIdentifier);
        }

        final Optional<State> loadedState = stateService.load(stateIdentifier);
        if (!loadedState.isPresent()) {
            throw new ServiceException("Found no state with identifier " + stateIdentifier);
        }

        final City newCity = new City(loadedState.get(), name, identifier, imagePath);

        return cityRepository.save(newCity);
    }
}
