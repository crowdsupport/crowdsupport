package org.outofrange.crowdsupport.service.impl;

import com.google.common.eventbus.Subscribe;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.BaseDto;
import org.outofrange.crowdsupport.event.ClientEntityChangeEvent;
import org.outofrange.crowdsupport.event.ClientEvent;
import org.outofrange.crowdsupport.event.EventDispatcher;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class WebSocketServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static final String DTO_PACKAGE = "org.outofrange.crowdsupport.dto.";
    private static final String TOPIC_PREFIX = "/topic/";

    private final SimpMessagingTemplate template;
    private final ModelMapper mapper;

    @Inject
    public WebSocketServiceImpl(SimpMessagingTemplate template, ModelMapper mapper) {
        this.template = template;
        this.mapper = mapper;

        EventDispatcher.register(this);
    }

    @Subscribe
    public void clientEvent(ClientEvent event) {
        if (event == null) {
            return;
        }

        final ClientEvent mappedEvent;
        if (event instanceof ClientEntityChangeEvent) {
            mappedEvent = mapPayloadToDto((ClientEntityChangeEvent) event);
        } else {
            mappedEvent = event;
        }

        event.getTopics().forEach(t -> {
            final String topic = TOPIC_PREFIX + t;
            log.debug("Sending event to: {} - Event: {}", topic, mappedEvent);
            template.convertAndSend(topic, mappedEvent);
        });
    }

    @SuppressWarnings("unchecked")
    private ClientEntityChangeEvent<BaseDto> mapPayloadToDto(ClientEntityChangeEvent event) {
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

            return new ClientEntityChangeEvent<>(event.getChangeType(), payloadDto,  event.getTopics());
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new ServiceException("Couldn't automatically map payload " + payloadClass.getSimpleName() + " to DTO (guessed name: "
                    + classNameGuess + ")", e);
        }
    }
}
