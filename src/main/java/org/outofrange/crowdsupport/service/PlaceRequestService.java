package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.PlaceRequest;

public interface PlaceRequestService extends BaseService<PlaceRequest> {
    PlaceRequest requestNewPlace(PlaceRequest placeRequest);
}
