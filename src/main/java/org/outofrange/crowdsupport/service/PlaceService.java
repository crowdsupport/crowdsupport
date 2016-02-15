package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.persistence.PlaceRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class to interact with places.
 */
public interface PlaceService {
    /**
     * Loads a place by the identifiers of itself, its city and its state.
     *
     * @param stateIdentifier the identifier of the state
     * @param cityIdentifier  the identifier of the city
     * @param placeIdentifier the identifier of the place
     * @return the loaded place, or {@link Optional#empty()} if none could be found
     * @throws NullPointerException     when one of the arguments is null
     * @throws IllegalArgumentException if one of the arguments is empty
     */
    Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier);

    /**
     * Deletes a place with a given id, or does nothing if there is no such place.
     *
     * @param id the id of the place
     */
    void deletePlace(long id);

    /**
     * Add an enabled user to the team of a place.
     * <p>
     * If the user is already part of the team, the method will do nothing.
     *
     * @param placeId  the id of the place of the team
     * @param username the username
     * @return the saved place
     * @throws NullPointerException     if {@code username} is null
     * @throws IllegalArgumentException if {@code username} is empty
     * @throws ServiceException         when no place or user could be found
     */
    Place addUserToTeam(long placeId, String username);

    /**
     * Loads a place for a given id
     *
     * @param id the id of the place
     * @return the loaded place, or null if none could be found
     */
    Place loadPlace(long id);

    /**
     * Removes an enabled user from the team of a place.
     * <p>
     * If the user isn't part of the team, the method will do nothing.
     *
     * @param placeId  the id of the place of the team
     * @param username the username
     * @return the saved place
     * @throws NullPointerException     if {@code username} is null
     * @throws IllegalArgumentException if {@code username} is empty
     * @throws ServiceException         when no place or user could be found
     */
    Place removeUserFromTeam(long placeId, String username);

    /**
     * Add a donation request to a place.
     *
     * @param placeId         the id of the place
     * @param donationRequest the donation request to add
     * @return the saved donation request
     * @throws NullPointerException if {@code donationRequest} is null
     */
    DonationRequest addDonationRequest(long placeId, DonationRequest donationRequest);

    /**
     * Saves an instance of a place in the repository
     *
     * @param place the place to save
     * @return the saved place
     */
    Place save(Place place);

    /**
     * Returns all persisted places.
     *
     * @return all persisted places
     */
    List<Place> loadAll();

    /**
     * Loads a bunch of places that could be associated with {@code text}.
     * <p>
     * When passing in either an empty string or null, an empty list will be returned immediately.
     * <p>
     * For a description of how the places are found, have a look at
     * {@link PlaceRepository#findPlacesRelatedToText(String)}
     *
     * @param text the text to use for searching
     * @return
     */
    List<Place> loadPlaceSuggestions(String text);
}
