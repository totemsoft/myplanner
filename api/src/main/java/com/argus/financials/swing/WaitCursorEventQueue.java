/*
 * WaitCursorEventQueue.java
 *
 * Created on 2 October 2002, 12:46
 */

package com.argus.financials.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.MenuContainer;

import javax.swing.SwingUtilities;

public class WaitCursorEventQueue extends EventQueue {

    private static final Cursor waitCursor = Cursor
            .getPredefinedCursor(Cursor.WAIT_CURSOR);

    private int delay;

    private WaitCursorTimer waitTimer;

    public WaitCursorEventQueue(int delay) {
        this.delay = delay;
        waitTimer = new WaitCursorTimer();
        waitTimer.setDaemon(true);
        waitTimer.start();
    }

    protected void dispatchEvent(AWTEvent event) {
        waitTimer.startTimer(event.getSource());
        try {
            super.dispatchEvent(event);
        } finally {
            waitTimer.stopTimer();
        }
    }

    private class WaitCursorTimer extends Thread {

        private Object source;

        private Component parent;

        synchronized void startTimer(Object source) {
            this.source = source;
            notify();
        }

        synchronized void stopTimer() {
            if (parent == null)
                interrupt();
            else {
                parent.setCursor(null);
                parent = null;
            }
        }

        public synchronized void run() {
            while (true) {
                try {
                    // wait for notification from startTimer()
                    wait();

                    // wait for event processing to reach the threshold, or
                    // interruption from stopTimer()
                    wait(delay);

                    if (source instanceof Component)
                        parent = SwingUtilities.getRoot((Component) source);
                    else if (source instanceof MenuComponent) {
                        MenuContainer mParent = ((MenuComponent) source)
                                .getParent();
                        if (mParent instanceof Component)
                            parent = SwingUtilities
                                    .getRoot((Component) mParent);
                    }

                    if (parent != null && parent.isShowing())
                        if (!parent.getCursor().equals(waitCursor))
                            parent.setCursor(waitCursor);

                } catch (InterruptedException ie) {
                }
            }
        }

    }

}
