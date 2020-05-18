/*
 * AppDefaultFocusManager.java
 *
 * Created on 9 September 2002, 14:17
 */

package com.argus.financials.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Component;
import java.awt.event.KeyEvent;

public class FinancialPlannerFocusManager extends javax.swing.DefaultFocusManager {

    /** Creates new AppDefaultFocusManager */
    public FinancialPlannerFocusManager() {
    }

    public void processKeyEvent(Component focusedComponent, KeyEvent anEvent) {

        Component c = anEvent.getComponent();
        /*
         * Prevent textarea and textpane from accepting tab and enter key codes.
         * Tab and Enter are special characters for mail merge. If any control
         * of of these controls had accepted enter or tab keycodes mail merge
         * would fail.
         */
        if ((c instanceof javax.swing.JTextPane || c instanceof javax.swing.JTextArea)
                && (anEvent.getKeyCode() == KeyEvent.VK_ENTER || anEvent
                        .getKeyCode() == KeyEvent.VK_TAB)) {
            anEvent.consume();
            return;
        }
        super.processKeyEvent(focusedComponent, anEvent);

        if (anEvent.getKeyCode() == KeyEvent.VK_ENTER)
            super.processKeyEvent(focusedComponent, new KeyEvent(anEvent
                    .getComponent(), anEvent.getID(), anEvent.getWhen(),
                    anEvent.getModifiers(), KeyEvent.VK_TAB));

        if (anEvent.getKeyCode() == KeyEvent.VK_TAB) {
            c = anEvent.getComponent();
            if (c instanceof javax.swing.JTextField) {
                javax.swing.JTextField tf = (javax.swing.JTextField) c;
                tf.select(0, tf.getText().length());
            }
        }

    }

    // Return true if a should be before b in the "tab" order.
    // Override this method if you want to change the automatic "tab" order.
    // The default implementation will order tab to give a left to right, top
    // down order.
    // Override this method if another order is required.
    public boolean compareTabOrder(Component a, Component b) {

        if (!super.compareTabOrder(a, b))
            return false;

        boolean aEnabled = a.isEnabled();
        boolean bEnabled = b.isEnabled();

        if (a instanceof javax.swing.text.JTextComponent)
            aEnabled = ((javax.swing.text.JTextComponent) a).isEditable();
        if (b instanceof javax.swing.text.JTextComponent)
            bEnabled = ((javax.swing.text.JTextComponent) b).isEditable();

        return bEnabled;

    }

    public void focusNextComponent(Component aComponent) {
        super.focusNextComponent(aComponent);
    }

}