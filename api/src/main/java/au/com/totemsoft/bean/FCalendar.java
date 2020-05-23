package au.com.totemsoft.bean;

/*
 * Class.java
 *
 * Created on 15 August 2002, 22:27
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JCalendar;

/**
 * RoundButton - a class that produces a lightweight button.
 */

public class FCalendar extends javax.swing.JDialog implements
        PropertyChangeListener {

    private static JCalendar sJCalendar;

    private static Calendar sCalendar;

    private static FDateChooser sDateField;

    private static FCalendar sInstance = null;

    String label; // The Button's text

    protected boolean pressed = false; // true if the button is detented.

    /**
     * implements Constructs a RoundButton with the specified label.
     * 
     * @param label
     *            the label of the button
     */

    public FCalendar(FDateChooser dateField) {
        super();
        init(dateField);
    }

    public FCalendar(FDateChooser dateField, java.awt.Frame parent) {
        super(parent);
        init(dateField);
    }

    public FCalendar(FDateChooser dateField, java.awt.Dialog parent) {
        super(parent);
        init(dateField);
    }

    private void init(FDateChooser dateField) {
        sDateField = dateField;

        sJCalendar = new JCalendar();

        this.getContentPane().add(sJCalendar);
        this.setModal(true);
        sJCalendar.setBorder(new javax.swing.border.EtchedBorder());
        this.setTitle("Select Date");
        this.pack();
    }

    public static FCalendar getInstance(FDateChooser dateField) {
        java.awt.Window parent = javax.swing.SwingUtilities
                .getWindowAncestor(dateField);
        if (parent instanceof java.awt.Frame)
            sInstance = new FCalendar(dateField, (java.awt.Frame) parent);
        else if (parent instanceof java.awt.Dialog)
            sInstance = new FCalendar(dateField, (java.awt.Dialog) parent);
        else
            sInstance = new FCalendar(dateField);
        sJCalendar.addPropertyChangeListener(sInstance);

        setDateField(dateField);

        // Get current field contetnt
        String currentValue = dateField.getDateField().getText();
        Calendar cal = Calendar.getInstance();
        Date currentDate = AbstractComponentModel.parseDate(currentValue);
        if (currentDate.equals(AbstractComponentModel.epochDate)) {
            currentDate = cal.getTime();
        }
        cal.setTime(currentDate);

        sJCalendar.setCalendar(cal);

        return sInstance;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("calendar")) {
            sCalendar = (Calendar) evt.getNewValue();
            SimpleDateFormat df = AbstractComponentModel.getDateFormatter();
            if (sDateField != null && sCalendar != null && df != null
                    && sCalendar.getTime() != null) {
                String format = df.format(sCalendar.getTime());
                if (format != null)
                    sDateField.setText(format);
            }
        }
        if (evt.getPropertyName().equals("close")) {
            sCalendar = (Calendar) evt.getNewValue();
            SimpleDateFormat df = AbstractComponentModel.getDateFormatter();
            sDateField.setText(df.format(sCalendar.getTime()));
            close();

        }

    }

    private void close() {
        sDateField = null;
        setVisible(false);
        sInstance.dispose();
    }

    public static void setDateField(FDateChooser dateF) {
        sDateField = dateF;
    }

}
