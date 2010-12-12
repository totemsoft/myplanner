/*
 * AbstractComponentModel.java
 *
 * Created on 29 August 2002, 18:35
 */

package com.argus.beans;

import java.util.HashMap;
import java.util.Vector;

import com.argus.financials.io.IOUtils2;
import com.argus.format.Currency;

/**
 * 
 * @version
 */
public abstract class AbstractComponentModel {

    private transient Vector modelChangedListeners;

    private transient Vector messageSentListeners;

    private java.util.HashMap componenList;

    private java.util.HashMap valueList;

    private boolean isModified = false;

    public static java.util.Date epochDate = new java.util.Date(0);

    /** Creates new AbstractComponentModel */
    public AbstractComponentModel() {
        componenList = new java.util.HashMap();
        valueList = new java.util.HashMap();

    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public void reset() {
        valueList = new java.util.HashMap();
        isModified = false;
    }

    public void put(String componentName, java.awt.Component component) {
        componenList.put(componentName, component);
        isModified = true;
    }

    /*
     * public void putValue(String componentName, String value){
     * valueList.put(componentName, value); isModified = true ; }
     */
    public Object putValue(String componentName, Object value) {
        Object oldValue = valueList.put(componentName, value);
        isModified = true;
        return oldValue;
    }

    public Object get(String componentName) {
        return componenList.get(componentName);
    }

    /*
     * public String getValue(String componentName){ String value = (String)
     * valueList.get(componentName); return value == null ? "": value ; }
     */
    public String getValue(String componentName) {
        Object value = valueList.get(componentName);
        return value == null ? "" : value.toString();
    }

    public Object getValueAsObject(String componentName) {
        Object value = valueList.get(componentName);
        return value;
    }

    public java.util.Date getDate(String componentName) {
        Object o = valueList.get(componentName);

        // java.util.Date value = java.util.Calendar.getInstance().getTime() ;

        java.util.Date value = epochDate;

        if (o == null)
            return value;

        value = parseDate((String) o);

        return value;
    }

    public java.util.Date getTrueDate(String componentName) {
        Object o = valueList.get(componentName);

        if (o == null)
            return null;

        return parseTrueDate((String) o);
    }

    public void setDate(String componentName, java.util.Date value) {

        java.text.SimpleDateFormat sdf = getDateFormatter();

        String sdate = sdf.format(value);

        if (sdate != null) {
            valueList.put(componentName, sdate);
            isModified = true;
        }

    }

    public void setTrueDate(String componentName, java.util.Date value) {
        if (value == null)
            valueList.put(componentName, value);
        else {
            java.text.SimpleDateFormat sdf = getDateFormatter();
            String sdate = sdf.format(value);
            valueList.put(componentName, sdate);
        }
        isModified = true;
    }

    public static java.text.SimpleDateFormat getDateFormatter() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy");
    }

    public static java.util.Date parseDate(String sDate) {

        java.util.Date value;

        try {
            // java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            // "dd/MM/yyyy" );
            java.text.SimpleDateFormat sdf = getDateFormatter();
            value = sdf.parse(sDate);
            // value =
            // java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).parse(sDate)
            // ;
        } catch (java.text.ParseException e) {

            value = epochDate;
            // value = java.util.Calendar.getInstance().getTime();
        }

        return value;

    }

    public static java.util.Date parseTrueDate(String sDate) {

        java.util.Date value;

        try {
            // java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            // "dd/MM/yyyy" );
            java.text.SimpleDateFormat sdf = getDateFormatter();
            value = sdf.parse(sDate);
            // value =
            // java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).parse(sDate)
            // ;
        } catch (java.text.ParseException e) {

            value = null;
            // value = java.util.Calendar.getInstance().getTime();
        }

        return value;

    }

    public double countDateDiff(java.util.Date d1, java.util.Date d2) {
        double diff = 0.00;
        if (d1 == null)
            return diff;
        if (d2 == null)
            return diff;

        diff = (double) (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24);
        return diff;
    }

    public boolean containsKey(String key) {
        return valueList.containsKey(key);
    }

    public abstract void calculate(String whoIsChanged);

    public boolean validate(String whoIsChanged) {
        return true;
    };

    public void postCalculate() {
    };

    public java.util.HashMap getComponents() {
        return componenList;
    }

    public java.util.HashMap getValues() {
        return valueList;
    }

    public double getDouble(String componentName) {
        Object o = valueList.get(componentName);

        double value = 0.00;

        if (o == null || !(o instanceof String) || ((String) o).trim() == "")
            return value;

        try {
            Currency formatter = Currency.createCurrencyInstance();
            formatter.setMaximumFractionDigits(0);
            formatter.setMinimumFractionDigits(0);
            value = formatter.doubleValue((String) o);

            // value = (new Double((String) o )).doubleValue() ;
        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace(System.out);
        }

        return value;
    }

    public void setDouble(String componentName, double value) {

        try {
            Currency formatter = Currency.createCurrencyInstance();
            formatter.setMaximumFractionDigits(1);
            formatter.setMinimumFractionDigits(1);
            String text = formatter.toString(value);

            // (new Double(value)).toString();

            valueList.put(componentName, text);
            isModified = true;
        } catch (java.lang.NumberFormatException e) {
        }
    }

    public void setDouble(String componentName, double value,
            int decimalPointNubmer) {

        try {
            Currency formatter = Currency.createCurrencyInstance();
            formatter.setMaximumFractionDigits(decimalPointNubmer);
            formatter.setMinimumFractionDigits(decimalPointNubmer);
            String text = formatter.toString(value);

            // (new Double(value)).toString();

            valueList.put(componentName, text);
            isModified = true;
        } catch (java.lang.NumberFormatException e) {
        }
    }

    public void setInt(String componentName, double value) {

        try {
            Currency formatter = Currency.createCurrencyInstance();
            formatter.setMaximumFractionDigits(0);
            formatter.setMinimumFractionDigits(0);
            String text = formatter.toString(value);

            // (new Double(value)).toString();

            valueList.put(componentName, text);
            isModified = true;
        } catch (java.lang.NumberFormatException e) {
        }
    }

    public void sendNotification(Object source) {
        // Inform all interested parties that data model has been updated
        fireModelDataChanged(new com.argus.beans.ModelDataChangedEvent(
                new ModelDataChanged(source)));
    }

    public synchronized void removeModelDataChangedListener(
            ModelDataChangedListener l) {
        if (modelChangedListeners != null && modelChangedListeners.contains(l)) {
            Vector v = (Vector) modelChangedListeners.clone();
            v.removeElement(l);
            modelChangedListeners = v;
        }
    }

    public synchronized void addModelDataChangedListener(
            ModelDataChangedListener l) {

        Vector v = modelChangedListeners == null ? new Vector(2)
                : (Vector) modelChangedListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            modelChangedListeners = v;
        }

    }

    protected void fireModelDataChanged(ModelDataChangedEvent e) {
        if (modelChangedListeners != null) {
            Vector listeners = modelChangedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((ModelDataChangedListener) listeners.elementAt(i))
                        .modelDataChanged(e);
            }
        }
    }

    // The following is a implementation of communication between datamodel and
    // visual components

    public void sendMessage(Object source, String messageType, String message) {
        // Inform all interested parties that data model has been updated
        fireMessageSent(new com.argus.beans.MessageSentEvent(new MessageSent(
                source, messageType, message)));
    }

    public synchronized void removeMessageSentListener(MessageSentListener l) {
        if (messageSentListeners != null && messageSentListeners.contains(l)) {
            Vector v = (Vector) messageSentListeners.clone();
            v.removeElement(l);
            messageSentListeners = v;
        }
    }

    public synchronized void addMessageSentListener(MessageSentListener l) {

        Vector v = messageSentListeners == null ? new Vector(2)
                : (Vector) messageSentListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            messageSentListeners = v;
        }

    }

    protected void fireMessageSent(MessageSentEvent e) {
        if (messageSentListeners != null) {
            Vector listeners = messageSentListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MessageSentListener) listeners.elementAt(i)).messageSent(e);
            }
        }
    }

    public String getValuesAsObject() {

        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(baos);

            os.writeObject(valueList);

            // test( baos );

            return baos.toString(IOUtils2.ENCODING_2_SERIALIZE);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    public void setValues(HashMap data) {
        if (data == null)
            return;
        valueList = new HashMap(data);
    }

    public void setValuesFromObject(String values) {

        // test( values );

        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
                    values.getBytes(IOUtils2.ENCODING_2_SERIALIZE));
            java.io.ObjectInputStream is = new java.io.ObjectInputStream(bais);

            valueList = (java.util.HashMap) is.readObject();

        } catch (Exception e) {
            // System.err.println( "AbstractComponentModel::setValuesFromObject(
            // '" + values + "' )" );
            System.err
                    .println("AbstractComponentModel::setValuesFromObject(...)"
                            + e.getMessage());
            valueList = new java.util.HashMap();
        }

    }

}
