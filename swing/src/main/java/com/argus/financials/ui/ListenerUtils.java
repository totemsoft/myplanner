/*
 * ListenerUtils.java
 *
 * Created on 5 September 2002, 16:55
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.ItemSelectable;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.MoneyCalc;

public class ListenerUtils {

    /** Creates new ListenerUtils */
    private ListenerUtils() {
    }

    /**
     * Listener utilities
     */
    public static void addListener(Component comp, MoneyCalc listener) {

        if (comp instanceof JTextComponent) {
            addDocumentListener((JTextComponent) comp, listener);

        } else if (comp instanceof com.argus.bean.FDateChooser) {
            addDocumentListener(((com.argus.bean.FDateChooser) comp)
                    .getDateField(), listener);

        } else if (comp instanceof ItemSelectable) {
            ((ItemSelectable) comp).addItemListener(listener);

        } else if (comp instanceof Container) {
            Container container = (Container) comp;
            for (int i = 0; i < container.getComponentCount(); i++)
                addListener(container.getComponent(i), listener);

        }

    }

    public static void addDocumentListener(JTextComponent text,
            final MoneyCalc listener) {

        if (!text.isEditable() || !text.isEnabled())
            return;

        // System.out.println(text.getAccessibleContext().getAccessibleName());

        Document doc = text.getDocument();
        if (doc.getProperty(DocumentNames.READY) == null)
            doc.putProperty(DocumentNames.READY, Boolean.TRUE);
        if (doc.getProperty(DocumentNames.NAME) == null)
            doc.putProperty(DocumentNames.NAME, text.getAccessibleContext()
                    .getAccessibleName());

        text.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Document doc = ((JTextComponent) evt.getSource()).getDocument();
                doc.putProperty(DocumentNames.READY, Boolean.FALSE);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                Document doc = ((JTextComponent) evt.getSource()).getDocument();
                doc.putProperty(DocumentNames.READY, Boolean.TRUE);
                listener.update(doc);
            }
        });

        doc.addDocumentListener(listener);

        /**
         * This allows the model to be safely rendered in the presence of
         * currency, if the model supports being updated asynchronously. The
         * given runnable will be executed in a way that allows it to safely
         * read the model with no changes while the runnable is being executed.
         * The runnable itself may not make any mutations.
         * 
         * Parameters: r - a Runnable used to render the model
         */
        // doc.render( this );
    }

}
