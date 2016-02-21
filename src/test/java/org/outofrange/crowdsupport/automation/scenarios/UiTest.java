package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.outofrange.crowdsupport.CleanDatabaseOnFailure;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.outofrange.crowdsupport.IntegrationTest;
import org.outofrange.crowdsupport.automation.data.DataProvider;
import org.outofrange.crowdsupport.automation.keyword.KeywordProvider;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import javax.inject.Inject;

@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest
@Category(IntegrationTest.class)
public class UiTest {
    @Inject
    @Rule
    public CleanDatabaseOnFailure cleanDatabaseOnFailure;

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

    @Before
    public void setUp() {
        cleanDatabaseOnFailure.resetDatabase();
    }

    @After
    public void tearDown() {
        data.cleanUp();
        keywords.cleanUp();
    }
}
