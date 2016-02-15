package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.util.ServiceException;

import java.util.List;

public interface PlaceRequestService {
    /**
     * Loads all place requests which are associated to a non-activated place.
     *
     * @return all found place requests with a non-activated place
     */
    List<PlaceRequest> loadAllWithInactivePlace();

    /**
     * Creates a new place request.
     *
     * @param placeRequest the place request to use for the request
     * @return the created place request.
     * @throws ServiceException if a city is set in {@code placeRequest}, but hasn't been found in the repository
     */
    PlaceRequest requestNewPlace(PlaceRequest placeRequest);

    /**
     * Saves an existing place request and sets the place to active.
     * <p>
     *
     * @param placeRequest the place request to activate
     * @return the saved place request
     * @throws ServiceException if
     *                          <ul>
     *                          <li>no city like the through placeRequest passed could be found</li>
     *                          <li>the persisted city differs from the passed city</li>
     *                          <li>no place is associated with the request</li>
     *                          <li>there is already an active place using the same identifier in the same city</li>
     *                          </ul>
     */
    PlaceRequest saveNewPlace(PlaceRequest placeRequest);

    /**
     * Declines a place request and deletes the associated, inactive place.
     *
     * @param placeRequestId the id of the place request to decline
     * @throws ServiceException if no place request has been found, or the place is already active
     */
    void declinePlaceRequest(long placeRequestId);

    /**
     * Loads all place requests.
     *
     * @return all place requests
     */
    List<PlaceRequest> loadAll();
}
