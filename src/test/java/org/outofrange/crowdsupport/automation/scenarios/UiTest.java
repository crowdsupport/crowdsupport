package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.After;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.outofrange.crowdsupport.automation.data.DataProvider;
import org.outofrange.crowdsupport.automation.keyword.KeywordProvider;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import javax.inject.Inject;

@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest
public class UiTest {
    @Inject
    private DataProvider data;

    @Inject
    private KeywordProvider keywords;

    protected DataProvider data() {
        return data;
    }

    protected KeywordProvider keywords() {
        return keywords;
    }

    @After
    public void tearDown() {
        data.cleanUp();
        keywords.cleanUp();
    }
}
