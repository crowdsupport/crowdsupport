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

        public static ClientEntityChangeEvent<State> stateChanged(ChangeEvent.Type type, State state) {
            return new ClientEntityChangeEvent<>(type, state, "state");
        }

        public static ClientEntityChangeEvent<City> cityChanged(ChangeEvent.Type type, City city) {
            return new ClientEntityChangeEvent<>(type, city, "city");
        }

        public static ClientEntityChangeEvent<Place> placeChanged(ChangeEvent.Type type, Place place) {
            return new ClientEntityChangeEvent<>(type, place, "place");
        }

        public static ClientEntityChangeEvent<User> userChanged(ChangeEvent.Type type, User user) {
            return new ClientEntityChangeEvent<>(type, user, "user");
        }

        public static ClientEntityChangeEvent<DonationRequest> donationRequestChanged(ChangeEvent.Type type, DonationRequest donationRequest) {
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

            public PlaceEvent<CommentDto> commentChange(ChangeEvent.Type changeType, CommentDto payload) {
                return new PlaceEvent<>(changeType, payload, place);
            }

            public PlaceEvent<DonationRequestDto> donationRequestChange(ChangeEvent.Type changeType, DonationRequestDto payload) {
                return new PlaceEvent<>(changeType, payload, place);
            }
        }
    }

    public static class EmptyClientEvent implements ClientEvent {
        private final String topic;

        private EmptyClientEvent(String topic) {
            this.topic = topic;
        }

        @Override
        public String getTopic() {
            return topic;
        }
    }

    public static class ClientEntityChangeEvent<T> extends EntityChangeEvent<T> implements ClientEvent {
        private final String topic;

        public ClientEntityChangeEvent(Type changeType, T payload, String topic) {
            super(changeType, payload);
            this.topic = topic;
        }

        @Override
        public String getTopic() {
            return topic;
        }
    }

    public static class PlaceEvent<T> extends ClientEntityChangeEvent<T> {
        private PlaceEvent(Type changeType, T payload, Place place) {
            super(changeType, payload, place.getCity().getState().getIdentifier() + "/" +
                    place.getCity().getIdentifier() + "/" + place.getIdentifier());
        }
    }
}
