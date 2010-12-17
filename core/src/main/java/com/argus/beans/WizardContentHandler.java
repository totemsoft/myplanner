/*
 * WizardContentHandler.java
 *
 * Created on 29 April 2003, 13:56
 */

package com.argus.beans;

import java.awt.Color;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.argus.util.KeyValue;

/**
 * 
 * @version
 */
public class WizardContentHandler extends java.awt.CardLayout implements
        java.awt.event.MouseListener, java.awt.event.ActionListener,
        DocumentListener {

    private transient Vector dataChangedListeners;

    private JPanel panel = new JPanel();

    private JList list = new JList();

    private Vector keyValues = new Vector();

    private Vector menuItems = new Vector();

    private JLabel label = new JLabel();

    private java.awt.Container parent = new java.awt.Container();

    private FTextField step;

    private int wizardPointer = 1;

    // Panel is curently visible on the wizard screen
    private int panelPointer = 1;

    private JButton buttonNext = null;

    private JButton buttonPrev = null;

    private String name = "WizardHontentHandler";

    private java.awt.Font font = null;

    private java.awt.Color color = null;

    /** Creates new WizardContentHandler */
    public WizardContentHandler(java.awt.Container parent, int vgap, int hgap) {
        super(vgap, hgap);
        this.parent = parent;
        initComponents();
    }

    public WizardContentHandler(java.awt.Container parent) {
        this(parent, 0, 0);
    }

    public void addItem(KeyValue item) {
        JCheckBox cb = new JCheckBox(item.toString());
        cb.setRolloverEnabled(true);
        cb.addActionListener(this);
        cb.addMouseListener(this);
        cb.setName((String) item.getValue());
        menuItems.add(cb);
        keyValues.add(item.getValue());
        panel.add(cb);
    }

    private void initComponents() {

        panel.setLayout(new javax.swing.BoxLayout(panel,
                javax.swing.BoxLayout.Y_AXIS));

        label.setBackground(panel.getBackground());
        label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        label.setFont(new java.awt.Font("Arial", 1, 24));

        step = new FTextField();
        step.getDocument().addDocumentListener(this);
        step.setText("1");

        // panel.add(list) ;
        // list.setListData(menuItems) ;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public void setButtonNext(JButton buttonNext) {
        this.buttonNext = buttonNext;
        this.buttonNext.addActionListener(this);
    }

    public void setButtonPrevious(JButton buttonPrev) {
        this.buttonPrev = buttonPrev;
        this.buttonPrev.addActionListener(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /*
     * Override super class method to capture current wizard position
     * 
     */
    public void show(Container parent0, String name) {

        panelPointer = keyValues.indexOf(name) + 1;

        processMenu();
        processButtons();
        processLabel();
        super.show(parent0, name);

    }

    /*
     * Override super class method to capture current wizard position
     * 
     */

    public void next(Container parent0) {

        String name = (String) keyValues.get(panelPointer);
        show(parent0, name);
        // super.next(parent);
        // panelPointer = panelPointer + 1;
        processMenu();
        processButtons();
        processLabel();

    }

    /*
     * Override super class method to capture current wizard position
     * 
     */

    public void previous(Container parent0) {
        // super.previous(parent);
        panelPointer = panelPointer - 1;

        String name = (String) keyValues.get(panelPointer - 1);

        panelPointer = panelPointer - 1;
        show(parent0, name);

        processMenu();
        processButtons();
        processLabel();

    }

    /*
     * Override super class method to capture current wizard position
     * 
     */

    public void last(Container parent) {
        super.last(parent);
        panelPointer = menuItems.size();
        processMenu();
        processButtons();
        processLabel();

    }

    /*
     * Override super class method to capture current wizard position
     * 
     */

    public void first(Container parent) {
        super.first(parent);
        panelPointer = 1;
        processMenu();
        processButtons();
        processLabel();

    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if (source instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) source;

            cb.setForeground(color);
            // cb.setFont(font);
        }

    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {

        Object source = mouseEvent.getSource();
        if (source instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) source;

            // font = cb.getFont();
            color = cb.getForeground();
            cb.setForeground(java.awt.Color.blue);
            // cb.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

        Object source = actionEvent.getSource();
        if (source instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) source;
            cb.setSelected(!cb.isSelected());
            show(parent, cb.getName());
        }

        if (source instanceof JButton) {
            JButton btn = (JButton) source;
            if (btn == this.buttonNext) {
                next(parent);

            } else if (btn == this.buttonPrev) {
                previous(parent);

            }

        }

    }

    public synchronized void removeFormDataChangedListener(
            FormDataChangedListener l) {
        if (dataChangedListeners != null && dataChangedListeners.contains(l)) {
            Vector v = (Vector) dataChangedListeners.clone();
            v.removeElement(l);
            dataChangedListeners = v;
        }
    }

    public synchronized void addFormDataChangedListener(
            FormDataChangedListener l) {
        Vector v = dataChangedListeners == null ? new Vector(2)
                : (Vector) dataChangedListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            dataChangedListeners = v;
        }
    }

    protected void fireFormDataChanged(FormDataChangedEvent e) {
        if (dataChangedListeners != null) {
            Vector listeners = dataChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((FormDataChangedListener) listeners.elementAt(i))
                        .formDataChanged(e);
            }
        }
    }

    public synchronized void registerComponent(String componentName,
            BasePanel prnt) {

        setName(componentName);
        prnt.registerComponent(componentName, step);
    }

    public void insertUpdate(DocumentEvent e) {
        processEvent(e);
    }

    public void removeUpdate(DocumentEvent e) {
        processEvent(e);
    }

    public void changedUpdate(DocumentEvent e) {
        processEvent(e);
    }

    private void processEvent(DocumentEvent e) {

        if (e.getType() != DocumentEvent.EventType.INSERT)
            return;
        javax.swing.text.Document doc = e.getDocument();
        int start = doc.getStartPosition().getOffset();
        int length = doc.getEndPosition().getOffset();
        String value = null;
        try {
            value = doc.getText(start, length);
        } catch (javax.swing.text.BadLocationException ble) {
            return;
        }

        try {
            wizardPointer = Integer.parseInt(value.trim());

            // wizardPointer = wizardPointer > menuItems.size() ?
            // menuItems.size(): wizardPointer ;

            wizardPointer = wizardPointer < 1 ? 1 : wizardPointer;

        } catch (Exception ex) {
            wizardPointer = 0;
        } finally {
        }

        processMenu();
        processButtons();
        processLabel();
    }

    /**
     * Changes buttons visibility and options avalabilty depending on current
     * value of wizardPointer variable
     * 
     */
    private void processMenu() {
        int size = menuItems.size();

        for (int i = 0; i < size; i++) {
            JCheckBox cb = (JCheckBox) menuItems.get(i);
            if (i < wizardPointer)
                cb.setEnabled(true);
            else
                cb.setEnabled(false);
            if (i + 1 < wizardPointer)
                cb.setSelected(true);
            else
                cb.setSelected(false);

        }

    }

    private void processLabel() {
        int size = menuItems.size();

        if (size == 0) {
            label.setText("");
            return;
        }
        panelPointer = panelPointer < 1 ? 1 : panelPointer;
        panelPointer = panelPointer >= size ? size : panelPointer;

        JCheckBox cb = (JCheckBox) menuItems.get(panelPointer - 1);
        if (cb != null && cb.getText() != null)
            label.setText(cb.getText());

        label.setBackground(panel.getBackground());
        label.setForeground(Color.lightGray);
        label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        label.setFont(new java.awt.Font("Arial", 1, 20));

    }

    private void processButtons() {

        int size = menuItems.size();
        panelPointer = panelPointer < 1 ? 1 : panelPointer;
        panelPointer = panelPointer >= size ? size : panelPointer;

        if (buttonPrev != null) {
            if (panelPointer > 1)
                buttonPrev.setEnabled(true);
            if (panelPointer == 1)
                buttonPrev.setEnabled(false);
        }

        if (buttonNext != null) {
            if (panelPointer < java.lang.Math.min(size, wizardPointer))
                buttonNext.setEnabled(true);
            if (panelPointer >= java.lang.Math.min(size, wizardPointer))
                buttonNext.setEnabled(false);
        }

    }

    public void pack() {
        processMenu();
        processButtons();
        processLabel();
    }

    public boolean isKeyPressed() {
        return false;
    }

}
