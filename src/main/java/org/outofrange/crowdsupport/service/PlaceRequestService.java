package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.PlaceRequest;

import java.util.List;

public interface PlaceRequestService {
    List<PlaceRequest> loadAllWithInactivePlace();

    PlaceRequest requestNewPlace(PlaceRequest placeRequest);

    PlaceRequest saveNewPlace(PlaceRequest placeRequest);

    void declinePlaceRequest(Long requestId);

    List<PlaceRequest> loadAll();
}
