package org.outofrange.crowdsupport.automation.keyword.ui;

import org.outofrange.crowdsupport.automation.keyword.ui.core.WebDriverWrapper;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class KeywordBase {
    @Inject
    private WebDriverWrapper webDriverWrapper;

    @Inject
    private ApplicationContext applicationContext;

    protected WebDriverWrapper web() {
        return webDriverWrapper;
    }

    protected <T extends KeywordBase> T getKeywords(Class<T> keywordClass) {
        return applicationContext.getBean(keywordClass);
    }
}
