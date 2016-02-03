package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.*;

public class Events {
    private Events() { /* no instantiation */ }

    public static EmptyClientEvent indexRequested() {
        return new EmptyClientEvent("indexRequested");
    }

    public static ClientEntityChangeEvent<State> state(ChangeType type, State state) {
        return new ClientEntityChangeEvent<>(type, state, "state");
    }

    public static ClientEntityChangeEvent<City> city(ChangeType type, City city) {
        return new ClientEntityChangeEvent<>(type, city, "city");
    }

    public static ClientEntityChangeEvent<Place> place(ChangeType type, Place place) {
        return new ClientEntityChangeEvent<>(type, place, "place");
    }

    public static ClientEntityChangeEvent<User> user(ChangeType type, User user) {
        return new ClientEntityChangeEvent<>(type, user, "user");
    }

    public static ClientEntityChangeEvent<Comment> comment(ChangeType type, Comment comment) {
        return new ClientEntityChangeEvent<>(type, comment,
                "comment", buildPlaceTopic(comment.getDonationRequest().getPlace()));
    }

    public static ClientEntityChangeEvent<DonationRequest> donationRequest(ChangeType type, DonationRequest donationRequest) {
        return new ClientEntityChangeEvent<>(type, donationRequest,
                "donationRequest", buildPlaceTopic(donationRequest.getPlace()));
    }

    private static String buildPlaceTopic(Place place) {
        return place.getCity().getState().getIdentifier() + "/" + place.getCity().getIdentifier() + "/" + place.getIdentifier();
    }
}
