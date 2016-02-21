package org.outofrange.crowdsupport.automation.data;

import org.flywaydb.core.Flyway;
import org.outofrange.crowdsupport.automation.ActionStack;
import org.outofrange.crowdsupport.automation.Cleanable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DataProvider implements Cleanable {
    private static final Logger log = LoggerFactory.getLogger(DataProvider.class);

    private static final ActionStack undoActions = new ActionStack();

    @Inject
    private UserDataProvider userDataProvider;
    @Inject
    private PlaceDataProvider placeDataProvider;
    @Inject
    private StateDataProvider stateDataProvider;
    @Inject
    private CityDataProvider cityDataProvider;

    @Inject
    private Flyway flyway;

    public UserDataProvider user() {
        return userDataProvider;
    }

    public StateDataProvider state() {
        return stateDataProvider;
    }

    public CityDataProvider city() {
        return cityDataProvider;
    }

    public PlaceDataProvider place() {
        return placeDataProvider;
    }

    @Override
    public void cleanUp() {
        undoActions.executeAll();
    }

    public static void registerUndo(Runnable runnable) {
        undoActions.addAction(runnable);
    }
}
