package org.outofrange.crowdsupport.service.impl;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class WebSocketServiceImplTest {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ModelMapper modelMapper;

    private WebSocketServiceImpl webSocketService;

    public void prepare() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        modelMapper = mock(ModelMapper.class);

        webSocketService = new WebSocketServiceImpl(simpMessagingTemplate, modelMapper);
    }

    @Test
    public void clientEventWithoutTopics() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void clientEventWithOneTopic() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void clientEventWithMultipleTopics() {
        throw new AssertionError("Not yet implemented");
    }

}