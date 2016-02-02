package org.outofrange.crowdsupport.service.impl;

import com.google.common.eventbus.Subscribe;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.event.ClientEvent;
import org.outofrange.crowdsupport.event.EventBusHolder;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.service.WebSocketService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static final String DTO_PACKAGE = "org.outofrange.crowdsupport.dto.";
    private static final String TOPIC_PREFIX = "/topic/";

    private final SimpMessagingTemplate template;
    private final ModelMapper mapper;

    @Inject
    public WebSocketServiceImpl(SimpMessagingTemplate template, ModelMapper mapper) {
        this.template = template;
        this.mapper = mapper;

        EventBusHolder.register(this);
    }

    @Override
    public void sendChangeToPlace(ChangeDto<?> change, Place place) {
        final String topic = "/topic/" + place.getCity().getState().getIdentifier() + "/" +
                place.getCity().getIdentifier() + "/" + place.getIdentifier();
        template.convertAndSend(topic, change);
    }

    @Subscribe
    public void clientEvent(ClientEvent event) {
        log.debug("Sending event to clients: {}", event);
        template.convertAndSend(TOPIC_PREFIX + event.getTopic(), event);
    }

    @Subscribe
    public void clientEntityChangeEvent(Events.ClientEntityChangeEvent event) {
        clientEvent(mapPayloadToDto(event));
    }

    @SuppressWarnings("unchecked")
    private Events.ClientEntityChangeEvent<BaseDto> mapPayloadToDto(Events.ClientEntityChangeEvent event) {
        if (event.getPayload() instanceof BaseDto) {
            // we don't need to map already mapped dtos
            return event;
        }

        final Class<?> payloadClass = event.getPayload().getClass();
        final String classNameGuess = DTO_PACKAGE + payloadClass.getSimpleName() + "Dto";

        try {
            final Class<?> dtoClass = Class.forName(classNameGuess);
            final BaseDto payloadDto = (BaseDto) mapper.map(event.getPayload(), dtoClass);

            log.debug("Mapped payload from {} to {}", payloadClass, dtoClass);

            return new Events.ClientEntityChangeEvent<>(event.getChangeType(), payloadDto, event.getTopic());
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new ServiceException("Couldn't automatically map payload " + payloadClass.getSimpleName() + " to DTO (guessed name: "
                    + classNameGuess + ")", e);
        }
    }
}
