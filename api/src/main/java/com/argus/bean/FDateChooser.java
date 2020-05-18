package com.argus.bean;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class FDateChooser extends JPanel implements DocumentListener,
        ValueSetter, FocusListener {
    BoxLayout borderLayout1 = new BoxLayout(this, BoxLayout.X_AXIS);

    private boolean iAmPressed = false;

    private boolean iAmChanged = false;

    private transient Vector dataChangedListeners;

    public static final String PropertyName = "ComponentName";

    private String pattern = "  /  /    ";

    private FTextField dateField;

    private JButton selectButton;

    public FDateChooser() {
        jbInit();
    }

    private void jbInit() {
        initDateField(null);
        initSelectButton(null);
    }

    private void initDateField(FTextField value) {

        if (dateField != null)
            return;

        dateField = value != null ? value : new FTextField();

        dateField.getDocument().addDocumentListener(this);
        dateField.addFocusListener(this);
        dateField.getDocument().putProperty(PropertyName, "FTextField");
        dateField.setFieldType(dateField.DATE);
        dateField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        add(dateField, 0);

    }

    private void initSelectButton(JButton value) {

        if (selectButton != null)
            return;

        selectButton = value != null ? value : new JButton() {
            public boolean isFocusTraversable() {
                return false;
            }
        };
        setLayout(borderLayout1);
        selectButton.setText("");

        selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/com/toedter/calendar/images/JCalendarColor16.gif")));
        selectButton.setMaximumSize(new java.awt.Dimension(16, 16));
        selectButton.setPreferredSize(new java.awt.Dimension(16, 16));
        selectButton.setMinimumSize(new java.awt.Dimension(10, 10));
        selectButton.setDefaultCapable(false);
        selectButton.setRequestFocusEnabled(true);
        selectButton.addFocusListener(this);
        add(selectButton, 1);

        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDateChooserDialog(evt);
            }
        });

    }

    public JTextField getDateField() {
        if (dateField == null)
            initDateField(null);
        return dateField;
    }

    public void setDateField(FTextField value) {
        initDateField(value);
    }

    public JButton getSelectButton() {
        if (selectButton == null)
            initSelectButton(null);
        return selectButton;
    }

    public void setSelectButton(JButton value) {
        initSelectButton(value);
    }

    private void showDateChooserDialog(java.awt.event.ActionEvent evt) {
        // Add your handling code here:

        java.awt.Rectangle bounds = dateField.getBounds();
        FCalendar fCalendar = FCalendar.getInstance(this);
        // fCalendar.setDateField(this);
        int width = fCalendar.getWidth();
        int heigth = fCalendar.getHeight();

        // Container cont = dateField.getParent();

        // Container cont = dateField.getTopLevelAncestor();

        Point p = dateField.getLocationOnScreen();

        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int xx = d.width > width + p.x ? p.x : p.x + dateField.getWidth()
                - width;
        int yy = d.height > p.y + dateField.getHeight() + heigth ? p.y
                + dateField.getHeight() : p.y - heigth;

        fCalendar.setBounds(xx, yy, width, heigth);
        fCalendar.show();
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
            BasePanel parent) {

        dateField.getDocument().putProperty(PropertyName, componentName);
        parent.registerComponent(componentName, this);
    }

    public boolean isKeyPressed() {
        return iAmPressed;
    }

    public BoxLayout getBorderLayout1() {
        return borderLayout1;
    }

    public void setBorderLayout1(BoxLayout borderLayout1) {
        this.borderLayout1 = borderLayout1;
    }

    public void setText(String value) {

        iAmChanged = true;
        getDateField().setText(value);

    }

    public String getText() {

        return getDateField().getText();

    }

    public javax.swing.text.Document getDocument() {

        return getDateField().getDocument();

    }

    public void setValue(Object str) {
        if (str != null)
            getDateField().setText(str.toString().trim());
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

        /*
         * javax.swing.text.Document doc = e.getDocument(); String title =
         * (String) doc.getProperty(PropertyName); int start =
         * doc.getStartPosition().getOffset() ; int length =
         * doc.getEndPosition().getOffset() ; String value = null; try { value =
         * doc.getText(start , length ) ; if
         * (value.trim().equals(pattern.trim())) value = "" ;
         *  } catch ( javax.swing.text.BadLocationException ble ) { return ;}
         * 
         * if (start == 0 && length == 1) return ;
         * 
         * iAmPressed = dateField.hasFocus() ;
         * 
         * if (iAmChanged == true) iAmPressed = true ;
         * 
         * fireFormDataChanged(new FormDataChangedEvent (new
         * FormDataChanged(title,value, iAmPressed,this))); iAmPressed = false ;
         * iAmChanged = false ; return ;
         */
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addFocusListener(FocusListener l) {
        super.addFocusListener(l);
        getDateField().addFocusListener(l); // to treat this control as
                                            // JTextField
        if (selectButton != null)
            selectButton.addFocusListener(l);
    }

    public void removeFocusListener(FocusListener l) {
        super.removeFocusListener(l);
        getDateField().removeFocusListener(l); // to treat this control as
                                                // JTextField
        if (selectButton != null)
            selectButton.removeFocusListener(l);
    }

    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        // Implement validations on Focus Lost Event

        if (focusEvent.isTemporary()) {
            return;
        }

        javax.swing.text.Document doc = getDocument();
        String title = (String) doc.getProperty(PropertyName);
        String value = getDateField().getText();

        if (value.trim().equals(pattern.trim()))
            value = "";

        iAmPressed = true;

        fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(title,
                value, iAmPressed, this)));
        iAmPressed = false;

    }

    public void focusGained(java.awt.event.FocusEvent focusEvent) {

        // if (focusEvent.getComponent() instanceof JButton )
        // selectButton.transferFocus() ;
    }

}