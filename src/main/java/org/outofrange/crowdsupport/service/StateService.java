package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.persistence.StateRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class StateService {
    @Inject
    private StateRepository stateRepository;
}
