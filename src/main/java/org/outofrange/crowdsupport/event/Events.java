package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.model.*;

/**
 * Factory class for all events.
 */
public class Events {
    private Events() { /* no instantiation */ }

    public static Event mailSettingsChanged(MailSettingsDto newSettings) {
        return new MailSettingsChangedEvent(newSettings);
    }

    public static Event indexRequested() {
        return new SimpleClientEvent("indexRequested");
    }

    public static Event state(ChangeType type, State state) {
        return new ClientEntityChangeEvent<>(type, state, "states");
    }

    public static Event city(ChangeType type, City city) {
        return new ClientEntityChangeEvent<>(type, city, "cities");
    }

    public static Event place(ChangeType type, Place place) {
        return new ClientEntityChangeEvent<>(type, place, "places");
    }

    public static Event user(ChangeType type, User user) {
        return new ClientEntityChangeEvent<>(type, user, "users");
    }

    public static Event comment(ChangeType type, Comment comment) {
        return new CommentClientEvent(type, comment, buildPlaceTopic(comment.getDonationRequest().getPlace()));
    }

    public static Event donationRequest(ChangeType type, DonationRequest donationRequest) {
        return new DonationRequestClientEvent(type, donationRequest, buildPlaceTopic(donationRequest.getPlace()));
    }

    public static Event donationRequestConfirmed() {
        return new SimpleClientEvent(1, "donationRequests/confirmed");
    }

    private static String buildPlaceTopic(Place place) {
        return place.getCity().getState().getIdentifier() + "/" + place.getCity().getIdentifier()
                + "/" + place.getIdentifier();
    }
}
