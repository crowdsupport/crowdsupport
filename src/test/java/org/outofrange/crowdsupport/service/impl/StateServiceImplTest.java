package org.outofrange.crowdsupport.service.impl;

import org.junit.Test;
import org.outofrange.crowdsupport.persistence.StateRepository;
import org.outofrange.crowdsupport.util.ServiceException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class StateServiceImplTest {
    private StateRepository stateRepository;

    private StateServiceImpl stateService;

    public void prepare() {
        stateRepository = mock(StateRepository.class);

        stateService = new StateServiceImpl(stateRepository);
    }

    @Test
    public void creatingStateWithCorrectArguments() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = ServiceException.class)
    public void creatingInvalidState() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = ServiceException.class)
    public void creatingAlreadyExistingState() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithUnknownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithKnownId() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithKnownIdentifier() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithIdentifierReturnsResult() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadWithIdentifierThrowsExceptionWhenEmptyPassed() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void loadWithIdentifierThrowsExceptionWhenNullPassed() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void searchStatesReturnsResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void searchStatesReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void searchStatesThrowsExceptionWhenNullPassed() {
        throw new AssertionError("Not yet implemented");
    }

}