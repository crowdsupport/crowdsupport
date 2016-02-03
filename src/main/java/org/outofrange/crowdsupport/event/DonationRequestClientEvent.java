package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;

public class DonationRequestClientEvent extends ClientEntityChangeEvent<DonationRequest> {
    public DonationRequestClientEvent(ChangeType changeType, DonationRequest donationRequest, String... furtherTopics) {
        super(changeType, donationRequest, "donationRequests", furtherTopics);

        if (changeType == ChangeType.CREATE) {
            addEvent(new SimpleClientEvent(1, "donationRequests/open"));
        } else if (changeType == ChangeType.DELETE) {
            if (donationRequest.isResolved()) {
                addEvent(new SimpleClientEvent(-1, "donationRequests/closed"));
            } else {
                addEvent(new SimpleClientEvent(-1, "donationRequests/open"));
            }
        }

        getTopics().add(buildPlaceTopic(donationRequest.getPlace()));
    }

    private static String buildPlaceTopic(Place place) {
        return place.getCity().getState().getIdentifier() + "/" + place.getCity().getIdentifier() + "/" + place.getIdentifier();
    }
}
