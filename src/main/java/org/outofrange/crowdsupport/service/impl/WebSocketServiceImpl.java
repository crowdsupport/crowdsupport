package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.service.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final SimpMessagingTemplate template;

    @Inject
    public WebSocketServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void sendChangeToPlace(ChangeDto<?> change, Place place) {
        final String topic = "/topic/" + place.getCity().getState().getIdentifier() + "/" +
                place.getCity().getIdentifier() + "/" + place.getIdentifier();
        template.convertAndSend(topic, change);
    }
}
