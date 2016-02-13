package org.outofrange.crowdsupport.automation.keyword;

import org.outofrange.crowdsupport.automation.Cleanable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class KeywordProvider implements Cleanable {
    @Inject
    private UiKeywordProvider uiKeywordProvider;

    public UiKeywordProvider ui() {
        return uiKeywordProvider;
    }

    @Override
    public void cleanUp() {
        uiKeywordProvider.cleanUp();
    }
}
