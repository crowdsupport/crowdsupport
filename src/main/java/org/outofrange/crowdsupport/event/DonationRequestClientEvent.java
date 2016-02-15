package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;

/**
 * An Event that propagates the change of a DonationRequest.
 * <p>
 * Will be published to specific topics.
 */
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
    }
}
