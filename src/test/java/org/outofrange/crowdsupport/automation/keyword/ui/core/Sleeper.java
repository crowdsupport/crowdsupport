package org.outofrange.crowdsupport.automation.keyword.ui.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleeper {
    private static final Logger log = LoggerFactory.getLogger(Sleeper.class);

    private Sleeper() { /* no instantiation */ }

    public static void sleep(long ms) {
        try {
            log.trace("Sleeping for {}ms", ms);

            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.warn("Was interrupted while sleeping", e);
        }
    }
}
