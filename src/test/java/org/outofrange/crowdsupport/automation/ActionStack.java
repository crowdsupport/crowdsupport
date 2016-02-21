package org.outofrange.crowdsupport.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class ActionStack {
    private static final Logger log = LoggerFactory.getLogger(ActionStack.class);

    private LinkedList<Runnable> actions = new LinkedList<>();

    public void addAction(Runnable action) {
        actions.addFirst(action);
    }

    public boolean executeAll() {
        boolean noExceptions = true;

        for (Runnable action : actions) {
            try {
                action.run();
            } catch (Exception e) {
                log.warn("Exception while executing {}", action, e);
                noExceptions = false;
            }
        }
        actions.clear();

        return noExceptions;
    }
}
