package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    private static final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;

    @Inject
    public CityServiceImpl(CityRepository cityRepository, StateRepository stateRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
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
    public City load(long id) {
        return cityRepository.findOne(id);
    }

    @Override
    public List<City> searchCities(String query) {
        return cityRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_CITIES)")
    public City createCity(String name, String identifier, String imagePath, String stateIdentifier) {
        log.debug("Creating new city with identifier {}", identifier);
        if (cityRepository.findOneByStateIdentifierAndIdentifier(stateIdentifier, identifier).isPresent()) {
            throw new ServiceException("There is already a city with an identifier of " + identifier +
                    " in a state with the identifier " + stateIdentifier);
        }

        final Optional<State> loadedState = stateRepository.findOneByIdentifier(stateIdentifier);
        if (!loadedState.isPresent()) {
            throw new ServiceException("Found no state with identifier " + stateIdentifier);
        }

        City newCity = new City(loadedState.get(), name, identifier);
        newCity.setImagePath(imagePath);
        newCity = cityRepository.save(newCity);

        Events.city(ChangeType.CREATE, newCity).publish();
        return newCity;
    }
}
