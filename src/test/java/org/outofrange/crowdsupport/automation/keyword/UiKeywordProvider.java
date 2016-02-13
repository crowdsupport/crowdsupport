package org.outofrange.crowdsupport.automation.keyword;

import org.outofrange.crowdsupport.automation.Cleanable;
import org.outofrange.crowdsupport.automation.keyword.ui.DriverHandler;
import org.outofrange.crowdsupport.automation.keyword.ui.SidePanelKeywords;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UiKeywordProvider implements Cleanable {
    @Inject
    private DriverHandler driverHandler;

    @Inject
    private SidePanelKeywords sidePanelKeywords;

    public SidePanelKeywords sidePanel() {
        return sidePanelKeywords;
    }

    @Override
    public void cleanUp() {
        driverHandler.stop();
    }
}
