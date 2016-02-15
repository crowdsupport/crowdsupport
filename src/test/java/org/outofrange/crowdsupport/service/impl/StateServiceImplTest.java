package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.service.ServiceException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class StateServiceImplTest {
    private final State state = new State("Name", "identifier");

    private StateRepository stateRepository;

    private StateServiceImpl stateService;

    @Before
    public void prepare() {
        stateRepository = mock(StateRepository.class);

        stateService = new StateServiceImpl(stateRepository);
    }

    @Test
    public void creatingStateWithCorrectArguments() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.empty());

        stateService.createState(state.getName(), state.getIdentifier(), state.getImagePath());

        verify(stateRepository).save(new State(state.getName(), state.getIdentifier()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingInvalidState() {
        stateService.createState("name", "invalid identifier", "path");
    }

    @Test(expected = ServiceException.class)
    public void creatingAlreadyExistingState() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.of(state));

        stateService.createState("name", state.getIdentifier(), "path");
    }

    @Test
    public void loadWithUnknownId() {
        when(stateRepository.findOne(1L)).thenReturn(null);

        assertNull(stateService.load(1));
    }

    @Test
    public void loadWithKnownId() {
        when(stateRepository.findOne(1L)).thenReturn(state);

        assertNotNull(stateService.load(1));
    }

    @Test
    public void loadWithKnownIdentifier() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.empty());

        assertFalse(stateService.load(state.getIdentifier()).isPresent());
    }

    @Test
    public void loadWithIdentifierReturnsResult() {
        when(stateRepository.findOneByIdentifier(state.getIdentifier())).thenReturn(Optional.of(state));

        assertEquals(state, stateService.load(state.getIdentifier()).get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadWithIdentifierThrowsExceptionWhenEmptyPassed() {
        stateService.load("");
    }

    @Test(expected = NullPointerException.class)
    public void loadWithIdentifierThrowsExceptionWhenNullPassed() {
        stateService.load(null);
    }

    @Test
    public void loadAllReturnsNothing() {
        when(stateRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(stateService.loadAll().isEmpty());
    }

    @Test
    public void loadAllReturnsResults() {
        when(stateRepository.findAll()).thenReturn(Collections.singletonList(state));

        final List<State> results = stateService.loadAll();

        assertThat(1, is(equalTo(results.size())));
        assertTrue(results.contains(state));
    }

    @Test
    public void searchStatesReturnsResults() {
        when(stateRepository.findAllByNameContainingIgnoreCase("query")).thenReturn(Collections.singletonList(state));

        final List<State> results = stateService.searchStates("query");

        assertThat(1, is(equalTo(results.size())));
        assertTrue(results.contains(state));
    }

    @Test
    public void searchStatesReturnsNothing() {
        when(stateRepository.findAllByNameContainingIgnoreCase("query")).thenReturn(Collections.emptyList());

        assertTrue(stateService.searchStates("query").isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void searchStatesThrowsExceptionWhenNullPassed() {
        stateService.searchStates(null);
    }
}