package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.Place;

public class PlaceEvent<T> extends ClientEntityChangeEvent<T> {
    public PlaceEvent(ChangeType changeType, T payload, Place place) {
        super(changeType, payload, place.getCity().getState().getIdentifier() + "/" +
                place.getCity().getIdentifier() + "/" + place.getIdentifier());
    }
}