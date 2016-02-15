package org.outofrange.crowdsupport.automation.data;

import org.outofrange.crowdsupport.automation.ActionStack;
import org.outofrange.crowdsupport.automation.Cleanable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DataProvider implements Cleanable {
    private static final ActionStack undoActions = new ActionStack();

    @Inject
    private UserDataProvider userDataProvider;
    @Inject
    private PlaceDataProvider placeDataProvider;
    @Inject
    private StateDataProvider stateDataProvider;
    @Inject
    private CityDataProvider cityDataProvider;

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
