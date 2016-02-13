package org.outofrange.crowdsupport.automation.keyword;

import org.outofrange.crowdsupport.automation.Cleanable;
import org.outofrange.crowdsupport.automation.keyword.ui.StatusKeywords;
import org.outofrange.crowdsupport.automation.keyword.ui.core.DriverHandler;
import org.outofrange.crowdsupport.automation.keyword.ui.SidePanelKeywords;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UiKeywordProvider implements Cleanable {
    @Inject
    private DriverHandler driverHandler;

    @Inject
    private SidePanelKeywords sidePanelKeywords;

    @Inject
    private StatusKeywords statusKeywords;

    public SidePanelKeywords sidePanel() {
        return sidePanelKeywords;
    }

    public StatusKeywords status() {
        return statusKeywords;
    }

    @Override
    public void cleanUp() {
        driverHandler.reset();
    }
}
