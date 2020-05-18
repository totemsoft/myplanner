package com.argus.bean;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.argus.format.Currency;

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
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public class FTextField extends JTextField implements DocumentListener,
        KeyListener, ValueSetter, FocusListener {

    private int columns = 10;

    private String type = this.WHOLE_NUMBER;

    public static final String WHOLE_NUMBER = "WholeNumber";

    public static final String ANY = "Any";

    public static final String DATE = "Date";

    private String pattern = "  /  /    ";

    private String format = "dd/MM/yyyy";

    BorderLayout borderLayout1 = new BorderLayout();

    private boolean iAmPressed = false;

    private boolean nonKeyBoard = false;

    private transient Vector dataChangedListeners;

    private Font font = null;

    public static final String PropertyName = "ComponentName";

    public FTextField() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.getDocument().addDocumentListener(this);
        this.addKeyListener(this);
        this.addFocusListener(this);
        this.getDocument().putProperty(PropertyName, "FTextField");
        this.setInputVerifier(new CurrencyVerifier());
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setFieldType(String type) {
        this.type = type;
        if (type.equals(DATE))
            this.setInputVerifier(new DateVerifier());
        else if (type.equals(WHOLE_NUMBER))
            this.setInputVerifier(new CurrencyVerifier());
        else if (type.equals(ANY)) {
            this.setInputVerifier(new CharacterVerifier());
            setColumns(50);
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
         * doc.getText(start , length ) ; } catch (
         * javax.swing.text.BadLocationException ble ) { return ;}
         * 
         * iAmPressed = this.hasFocus() ; if (iAmPressed == false )iAmPressed =
         * nonKeyBoard ;
         * 
         * fireFormDataChanged(new FormDataChangedEvent(new
         * FormDataChanged(title,value, iAmPressed,this))); iAmPressed = false ;
         */
    }

    public void setValue(Object obj) {
        if (obj != null)
            super.setText(obj.toString().trim());
    }

    public void setText(String value, boolean notify) {
        setText(value);
        focusLost(new FocusEvent(this, hashCode(), false));
    }

    public void setText(String t) {

        // Processing of programatically generated event
        nonKeyBoard = true;
        super.setText(t);
        nonKeyBoard = false;
    }

    public boolean isKeyPressed() {
        return iAmPressed;
    }

    public synchronized void registerComponent(String componentName,
            BasePanel parent) {

        getDocument().putProperty(PropertyName, componentName);
        parent.registerComponent(componentName, this);
    }

    public String getSourceName() {
        return (String) this.getDocument().getProperty(PropertyName);

    }

    public void keyPressed(java.awt.event.KeyEvent keyEvent) {
        iAmPressed = false;
        if (type.equals(DATE)) {
            processDateControls(keyEvent);
        }
    }

    public void keyReleased(java.awt.event.KeyEvent keyEvent) {
        iAmPressed = false;

    }

    public void keyTyped(java.awt.event.KeyEvent keyEvent) {
        iAmPressed = true;

        // Process numbers only
        if (type.equals(WHOLE_NUMBER)
                && !Character.isDigit(keyEvent.getKeyChar())
                && !keyEvent.isActionKey()
                && Character.getType(keyEvent.getKeyChar()) != Character.CONTROL) {
            keyEvent.consume();
        }

        String text = this.getText();

        // Process date in the format DD/MM/YYYY only
        if (type.equals(DATE)) {
            processDateChars(keyEvent);
        }

        if (text.length() >= columns
                && Character.getType(keyEvent.getKeyChar()) != Character.CONTROL) {
            keyEvent.consume();
            return;
        }

    }

    protected void processDateChars(java.awt.event.KeyEvent keyEvent) {
        String text = this.getText();

        char[] cText = text.toCharArray();
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();

        if (start != end) {
            cText = deleteChars(start, end, cText);
            this.setCaretPosition(start);
        }

        int pos = this.getCaretPosition();
        // Date is limited by 10 chars
        if (pos > 10) {
            keyEvent.consume();
            return;
        }

        // If key pressed is digid
        if (pos < 10 && keyEvent.getKeyChar() != Character.CONTROL
                && Character.isDigit(keyEvent.getKeyChar())) {
            if (cText[pos] == '/')
                pos = pos + 1;
            cText[pos] = keyEvent.getKeyChar();
            text = String.copyValueOf(cText);
            keyEvent.consume();
            this.setText(text);
            if (pos + 1 == 2 || pos + 1 == 5)
                pos = pos + 1;
            this.setCaretPosition(pos + 1);
        }
        // If key pressed is backspace
        else if (keyEvent.getKeyChar() == keyEvent.VK_BACK_SPACE) {

            pos = this.getCaretPosition() - 1;

            if (pos < 0) {
                keyEvent.consume();
                return;
            }

            if (cText[pos] == '/')
                pos = pos - 1;
            cText[pos] = ' ';
            text = String.copyValueOf(cText);
            keyEvent.consume();
            this.setText(text);
            this.setCaretPosition(pos);

        } else {
            keyEvent.consume();
        }

    }

    protected void processDateControls(java.awt.event.KeyEvent keyEvent) {
        // Ignore all shifts, alts and ctrls
        /*
         * if (keyEvent.getModifiers() > 0 ) { keyEvent.consume(); return ; };
         */
        if (keyEvent.getKeyCode() == keyEvent.VK_V
                && keyEvent.getModifiers() == keyEvent.CTRL_MASK) {

            JTextField target = new JTextField();
            target.paste();
            String clipContetnt = target.getText();
            if (clipContetnt.length() < 10) {
                keyEvent.consume();
                return;
            }
            keyEvent.consume();
            this.setSelectionStart(0);
            this.setSelectionEnd(10);
            this.setText(clipContetnt);
            this.transferFocus();
            return;
        }

        int pos = 0;
        String text = this.getText();
        char[] cText = text.toCharArray();

        // If key pressed is Delete

        if (keyEvent.getKeyCode() == 127) {

            int start = this.getSelectionStart();
            int end = this.getSelectionEnd();

            if (start == end)
                end = end + 1;
            cText = deleteChars(start, end, cText);
            text = String.copyValueOf(cText);

            keyEvent.consume();
            this.setText(text);
            this.setCaretPosition(start);
        }

    }

    private char[] deleteChars(int start, int end, char cText[]) {

        int pos = start;

        // Day field
        int i = 0;
        int startPos = 0;
        int endPos = 0;
        // Day field
        if (start < 3) {
            startPos = start;
            endPos = end > 2 ? 2 : end;
            // Remove selected text
            for (i = startPos; i < endPos; i++) {
                cText[i] = ' ';
            }
            // Copy remaining text
            for (i = endPos; i <= 1; i++, startPos++) {
                cText[startPos] = cText[i];
                cText[i] = ' ';
            }

        }
        // month field
        if (start < 6 && end > 3) {
            startPos = start < 3 ? 3 : start;
            endPos = end > 5 ? 5 : end;
            // Remove selected text
            for (i = startPos; i < endPos; i++) {
                cText[i] = ' ';
            }
            // Copy remaining text
            for (i = endPos; i <= 4; i++, startPos++) {
                cText[startPos] = cText[i];
                cText[i] = ' ';
            }

        }

        // year field
        if (end > 6) {
            startPos = start < 6 ? 6 : start;
            endPos = end;
            // Remove selected text
            for (i = startPos; i < endPos; i++) {
                cText[i] = ' ';
            }
            // Copy remaining text
            for (i = endPos; i <= 9; i++, startPos++) {
                cText[startPos] = cText[i];
                cText[i] = ' ';
            }

        }
        return cText;

    }

    public void focusLost(java.awt.event.FocusEvent focusEvent) {

        if (focusEvent.isTemporary()) {
            nonKeyBoard = true;
            return;
        }

        if (type.equals(DATE)) {
            // Nothing to do if component will regain focus
            this.setFont(font);
            if (this.getText().trim().equals(pattern.trim()))
                this.setText("");

        }

        // Implement validations on Focus Lost Event
        FTextField comp = (FTextField) focusEvent.getSource();

        javax.swing.text.Document doc = comp.getDocument();
        String title = (String) doc.getProperty(PropertyName);
        String value = comp.getText();

        iAmPressed = true;
        if (iAmPressed == false)
            iAmPressed = nonKeyBoard;

        fireFormDataChanged(new FormDataChangedEvent(new FormDataChanged(title,
                value, iAmPressed, this)));
        iAmPressed = false;

    }

    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        //

        if (type.equals(DATE) && this.isEditable() && this.isEnabled()) {

            if (this.getText().trim().equals(""))
                this.setText(pattern);

            if (font == null)
                font = this.getFont();
            this
                    .setFont(new Font("monospaced", font.getStyle(), font
                            .getSize()));
            this.setCaretPosition(0);
        }
    }

    public class DateVerifier extends InputVerifier {

        public DateVerifier() {
        }

        public boolean verify(JComponent comp) {

            if (comp instanceof FTextField) {

                FTextField field = (FTextField) comp;
                String text = field.getText();
                if (text.trim().equals("")
                        || text.trim().equals(pattern.trim()))
                    return true;
                try {

                    Calendar cal = Calendar.getInstance();

                    java.util.StringTokenizer token = new java.util.StringTokenizer(
                            text, "/");

                    int day = Integer.parseInt(token.nextToken());
                    int month = Integer.parseInt(token.nextToken());
                    int year = Integer.parseInt(token.nextToken());

                    if (day > cal.getMaximum(cal.DATE)) {
                        field.setCaretPosition(2);
                        field.moveCaretPosition(0);

                        return false;
                    }
                    if (month > cal.getMaximum(cal.MONTH) + 1) {
                        field.setCaretPosition(5);
                        field.moveCaretPosition(3);
                        return false;
                    }
                    if (year > cal.getMaximum(cal.YEAR) || year < 1800) {
                        field.setCaretPosition(10);
                        field.moveCaretPosition(6);

                        return false;
                    }

                    // Check if day of month is worng (e.g. 31/02/2003)
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                            format);
                    java.util.Date date = sdf.parse(text);
                    String dateAfter = sdf.format(date);
                    if (!dateAfter.equals(text)) {
                        field.setCaretPosition(2);
                        field.moveCaretPosition(0);
                        return false;
                    }
                    return true;
                } catch (Exception e) {
                    Toolkit.getDefaultToolkit().beep();
                    return false;
                }
            }

            return true;

        }
    }

    public class CurrencyVerifier extends InputVerifier {

        public CurrencyVerifier() {
        }

        public boolean verify(JComponent comp) {

            if (comp instanceof FTextField) {

                FTextField field = (FTextField) comp;
                String text = field.getText();
                if (text.trim().equals(""))
                    return true;
                try {

                    Currency formatter = Currency.createCurrencyInstance();
                    formatter.setMaximumFractionDigits(0);
                    formatter.setMinimumFractionDigits(0);
                    double value = formatter.doubleValue(text);
                    text = formatter.toString(value);

                    field.setText(text);
                } catch (Exception e) {
                    Toolkit.getDefaultToolkit().beep();
                    return false;
                }
            }

            return true;

        }
    }

    public class CharacterVerifier extends InputVerifier {

        public CharacterVerifier() {
        }

        public boolean verify(JComponent comp) {
            return true;
        }
    }

}