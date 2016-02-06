package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.event.ClientEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class WebSocketServiceImplTest {
    private SimpMessagingTemplate simpMessagingTemplate;

    private WebSocketServiceImpl webSocketService;

    private TestEvent clientEvent;

    @Before
    public void prepare() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);

        webSocketService = new WebSocketServiceImpl(simpMessagingTemplate, mock(ModelMapper.class));

        clientEvent = new TestEvent();
    }

    @Test
    public void clientEventWithoutTopics() {
        webSocketService.clientEvent(clientEvent);

        verifyZeroInteractions(simpMessagingTemplate);
    }

    @Test
    public void clientEventWithNullDoesNothing() {
        webSocketService.clientEvent(null);

        verifyZeroInteractions(simpMessagingTemplate);
    }

    @Test
    public void clientEventWithOneTopic() {
        clientEvent.addTopic("test1");

        webSocketService.clientEvent(clientEvent);

        verify(simpMessagingTemplate).convertAndSend("/topic/test1", clientEvent);
    }

    @Test
    public void clientEventWithMultipleTopics() {
        clientEvent.addTopic("test1").addTopic("test2");

        webSocketService.clientEvent(clientEvent);

        verify(simpMessagingTemplate).convertAndSend("/topic/test1", clientEvent);
        verify(simpMessagingTemplate).convertAndSend("/topic/test2", clientEvent);
    }

    private static class TestEvent implements ClientEvent {
        private final List<String> topics = new ArrayList<>();

        public TestEvent addTopic(String topic) {
            topics.add(topic);
            return this;
        }

        @Override
        public List<String> getTopics() {
            return topics;
        }
    }
}