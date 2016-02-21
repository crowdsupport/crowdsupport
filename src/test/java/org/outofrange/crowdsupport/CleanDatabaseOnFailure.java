package org.outofrange.crowdsupport;

import org.flywaydb.core.Flyway;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CleanDatabaseOnFailure extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(CleanDatabaseOnFailure.class);

    private final Flyway flyway;

    @Inject
    public CleanDatabaseOnFailure(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        log.warn("Testcase failed");

        resetDatabase();
    }

    public void resetDatabase() {
        log.info("Cleaning database");

        flyway.clean();
        flyway.migrate();
    }
}
