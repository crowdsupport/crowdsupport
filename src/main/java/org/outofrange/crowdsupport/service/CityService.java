package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    /**
     * Loads a city with the identifier of itself and its state.
     *
     * @param identifier      the identifier of the city
     * @param stateIdentifier the identifier of the state the city is in
     * @return the loaded city, or {@link Optional#empty()} if none could be found
     */
    Optional<City> load(String identifier, String stateIdentifier);

    /**
     * Loads a city
     *
     * @param id the id of the city
     * @return the loaded city, or null if none could be found
     */
    City load(long id);

    /**
     * Loads all cities where {@code query} can be found in the name. Ignores case.
     *
     * @param query the text to look for
     * @return all matching cities.
     * @throws NullPointerException if {@code query} is null
     */
    List<City> searchCities(String query);

    /**
     * Creates a new city.
     * <p>
     * The identifier has to be unique in the context of the state it's.
     *
     * @param name            the name of the city
     * @param identifier      the identifier of the city
     * @param imagePath       the image path of the city
     * @param stateIdentifier the state identifier of the city
     * @return the created city
     * @throws ServiceException if the state hasn't been found, or there is already a city with {@code identifier}
     *                          in the state identifier by {@code stateIdentifier}
     */
    City createCity(String name, String identifier, String imagePath, String stateIdentifier);

    /**
     * Loads all persisted cities.
     *
     * @return all persisted cities
     */
    List<City> loadAll();

    /**
     * Deletes a city if it's there, or does nothing otherwise.
     * @param id the id of the city
     */
    void deleteCity(long id);
}
