package org.outofrange.crowdsupport.automation;

import java.util.LinkedList;

public class ActionStack {
    private LinkedList<Runnable> actions = new LinkedList<>();

    public void addAction(Runnable action) {
        actions.addFirst(action);
    }

    public void executeAll() {
        actions.forEach(Runnable::run);
        actions.clear();
    }
}
