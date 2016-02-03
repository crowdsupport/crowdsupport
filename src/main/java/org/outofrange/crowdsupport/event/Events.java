package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.model.*;

public class Events {
    private Events() { /* no instantiation */ }

    public static class Client {
        private Client() { /* no instantiation */ }

        public static EmptyClientEvent indexRequested() {
            return new EmptyClientEvent("indexRequested");
        }

        public static ClientEntityChangeEvent<State> stateChanged(ChangeType type, State state) {
            return new ClientEntityChangeEvent<>(type, state, "state");
        }

        public static ClientEntityChangeEvent<City> cityChanged(ChangeType type, City city) {
            return new ClientEntityChangeEvent<>(type, city, "city");
        }

        public static ClientEntityChangeEvent<Place> placeChanged(ChangeType type, Place place) {
            return new ClientEntityChangeEvent<>(type, place, "place");
        }

        public static ClientEntityChangeEvent<User> userChanged(ChangeType type, User user) {
            return new ClientEntityChangeEvent<>(type, user, "user");
        }

        public static ClientEntityChangeEvent<DonationRequest> donationRequestChanged(ChangeType type, DonationRequest donationRequest) {
            return new ClientEntityChangeEvent<>(type, donationRequest, "donationRequest");
        }

        public static PlaceClientEvents place(Place place) {
            return new PlaceClientEvents(place);
        }

        public static class PlaceClientEvents {
            private final Place place;
            private PlaceClientEvents(Place place) {
                this.place = place;
            }

            public PlaceEvent<CommentDto> commentChange(ChangeType changeType, CommentDto payload) {
                return new PlaceEvent<>(changeType, payload, place);
            }

            public PlaceEvent<DonationRequestDto> donationRequestChange(ChangeType changeType, DonationRequestDto payload) {
                return new PlaceEvent<>(changeType, payload, place);
            }
        }
    }
}
