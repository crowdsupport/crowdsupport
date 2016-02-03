package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.*;

public class Events {
    private Events() { /* no instantiation */ }

    public static Event indexRequested() {
        return new EmptyClientEvent("indexRequested");
    }

    public static Event state(ChangeType type, State state) {
        return new ClientEntityChangeEvent<>(type, state, "state");
    }

    public static Event city(ChangeType type, City city) {
        return new ClientEntityChangeEvent<>(type, city, "city");
    }

    public static Event place(ChangeType type, Place place) {
        return new ClientEntityChangeEvent<>(type, place, "place");
    }

    public static Event user(ChangeType type, User user) {
        return new ClientEntityChangeEvent<>(type, user, "user");
    }

    public static Event comment(ChangeType type, Comment comment) {
        return new ClientEntityChangeEvent<>(type, comment,
                "comment", buildPlaceTopic(comment.getDonationRequest().getPlace()));
    }

    public static Event donationRequest(ChangeType type, DonationRequest donationRequest) {
        return new ClientEntityChangeEvent<>(type, donationRequest,
                "donationRequest", buildPlaceTopic(donationRequest.getPlace()));
    }

    private static String buildPlaceTopic(Place place) {
        return place.getCity().getState().getIdentifier() + "/" + place.getCity().getIdentifier() + "/" + place.getIdentifier();
    }
}
