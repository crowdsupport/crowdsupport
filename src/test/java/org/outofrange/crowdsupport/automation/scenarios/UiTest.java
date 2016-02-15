package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.After;
import org.junit.experimental.categories.Category;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.outofrange.crowdsupport.IntegrationTest;
import org.outofrange.crowdsupport.automation.data.DataProvider;
import org.outofrange.crowdsupport.automation.keyword.KeywordProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import javax.inject.Inject;

@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest
@Category(IntegrationTest.class)
public class UiTest {
    private static final Logger log = LoggerFactory.getLogger(UiTest.class);

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

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.warn("Error while sleeping", e);
        }
    }
}
