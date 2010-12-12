/*
 * DocumentWriter.java
 *
 * Created on 13 March 2002, 16:10
 */

package com.argus.financials.report;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.io.File;

import com.argus.financials.config.FPSLocale;
import com.argus.financials.io.OutputView;

public abstract class BaseDocument {

    protected final static boolean DEBUG;
    // false;
    // true;
    static {
        FPSLocale r = com.argus.financials.config.FPSLocale.getInstance();
        DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();
    }

    //
    protected Object data;

    // Map( Field field, Object value )
    private java.util.Map dataFields;

    /** Creates new Document */
    public BaseDocument(Object data) {
        setData(data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;

        if (this.data == null)
            return;

        java.io.PrintStream printStream = System.out;
        // if ( !FPSLocale.isDevelopment() ) {

        try {
            String className = this.data.getClass().getName();
            className = className.substring(className.lastIndexOf('.') + 1);

            String fileName = FPSLocale.getInstance().getLogDir() + File.separator +
                    className + ".parameters";

            OutputView view = new OutputView(fileName, true);
            /*
             * javax.swing.JDialog dlg = new javax.swing.JDialog(
             * (java.awt.Frame) null, "Output Parameters", false );
             * dlg.getContentPane().add( view ); dlg.pack();
             *  // position int w = (int) SwingUtils.DIM_SCREEN.getWidth(); int
             * h = (int) SwingUtils.DIM_SCREEN.getHeight(); dlg.setLocation( w -
             * (int) dlg.getWidth(), h - (int) dlg.getHeight() );
             * 
             * dlg.setVisible(true);
             */
            printStream = view.getPrintStream();

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }

        // }

        print(data.getClass(), printStream, null, 0, true);
        printStream.flush();
        printStream.close();

    }

    public static void print(Object data, java.io.PrintStream printStream) {
        if (data != null)
            print(data.getClass(), printStream, null, 0, false);
    }

    public static void print(Class class2print,
            java.io.PrintStream printStream, String declaringClasses) {
        print(class2print, printStream, declaringClasses, 0, false);
    }

    // e.g. declaringClasses = Strategy.Restructured.XXX.ZZZ
    private static void print(Class class2print,
            java.io.PrintStream printStream, String declaringClasses,
            int level, boolean filter) {

        if (class2print == null || printStream == null)
            return;

        if (DEBUG) {
            printStream
                    .println("==========================================================");
            printStream.println(class2print.getName());
            printStream.println(declaringClasses);
            printStream
                    .println("==========================================================");
        }

        java.util.Collection removePatterns = getDuplicatedPatterns();

        Class[] classes;
        if (level == 0)
            classes = new Class[] { class2print };
        else
            classes = class2print.getClasses();

        for (int i = 0; i < classes.length; i++) {
            Class c = classes[i];

            // if ( c.isInterface() )
            // continue;

            // get rid of top level class name
            String declaringClassName = level == 0 ? null : getName(c
                    .toString());

            String declaringClasses2 = declaringClasses;
            if (declaringClasses2 == null)
                declaringClasses2 = declaringClassName;
            else
                declaringClasses2 += "." + declaringClassName;

            /*
             * printStream.println(); printStream.println( "DeclaringClass: " +
             * declaringClass ); printStream.println( "DeclaringClassName: " +
             * declaringClassName ); printStream.println( "DeclaringClasses2: " +
             * declaringClasses2 ); //
             */

            java.lang.reflect.Field[] fields = c.getFields();
            for (int j = 0; j < fields.length; j++) {
                java.lang.reflect.Field f = fields[j];

                // Word Parameter Name
                String paramName;
                if (f.getType().equals(String.class)) {
                    if (declaringClasses2 == null)
                        paramName = getName(f.getName());
                    else
                        paramName = declaringClasses2 + "."
                                + getName(f.getName());

                } else if (f.getType().equals(java.util.Collection.class)
                        || f.getType().equals(
                                javax.swing.table.TableModel.class)) {
                    paramName = getName(f.getName());
                    int index = paramName.lastIndexOf('.');
                    if (index > 0)
                        paramName = paramName.substring(index + 1);
                    if (declaringClasses2 != null)
                        paramName = declaringClasses2 + "." + paramName;

                } else {
                    continue;
                }

                if (!filter || !findKeyToRemove(paramName, removePatterns))
                    printStream.println(paramName);
                // printStream.println( paramName.toUpperCase() );

            }

            print(c, printStream, declaringClasses2, level + 1, filter);

        }

    }

    // remove all packages and class name, (e.g. Client.Person.Title)
    // replace $ char with . (for inner classes)
    static java.lang.String getName(String className) {
        // return className.substring( className.indexOf('$') + 1,
        // className.length() ).replace('$', '.');

        int index = className.lastIndexOf('$');
        if (index < 0)
            index = className.lastIndexOf('.');
        return className.substring(index + 1);
    }

    static java.lang.String getName(java.lang.reflect.Field field) {
        // return getName( field.toString() );

        String className = field.toString();
        return className.substring(className.indexOf('$') + 1,
                className.length()).replace('$', '.');
    }

    /**
     * these two methods has to work together (as ctor/dtor)
     */
    public abstract void initialize() throws ReportException;

    public abstract void uninitialize() throws ReportException;

    public abstract void updateDocument() throws ReportException;

    /**
     * Map( Field field, Object value )
     */
    protected java.util.Map getFields() throws ReportException {

        if (dataFields == null) {
            dataFields = new java.util.TreeMap();
            getFields(data, dataFields);
        }

        return dataFields;

    }

    public static void getFields(Object data, java.util.Map map)
            throws ReportException {
        getFields(data, map, false, null, 0);
    }

    public static void getFields(Object data, java.util.Map map,
            boolean nullObjects, String declaringClasses, int level)
            throws ReportException {

        if (data == null || map == null)
            return;

        // new report
        if (data instanceof java.util.Map) {
            // copy ALL fields
            map.putAll((java.util.Map) data);
            return;
        }

        // old report (using reflection)
        try {

            java.lang.reflect.Field[] fields = data.getClass().getFields();
            if (fields == null || fields.length == 0)
                return;

            if (DEBUG) {
                System.out
                        .println("==========================================================");
                System.out.println(data.toString());
                System.out.println("declaringClasses=" + declaringClasses);
                System.out
                        .println("==========================================================");
            }

            // get rid of top level class name
            String declaringClassName = level == 0 ? null : getName(data
                    .getClass().toString());
            // if ( DEBUG ) System.out.println( "declaringClassName=" +
            // declaringClassName );

            String declaringClasses2 = declaringClasses;
            if (declaringClasses2 == null)
                declaringClasses2 = declaringClassName;
            else
                declaringClasses2 += "." + declaringClassName;

            for (int j = 0; j < fields.length; j++) {

                java.lang.reflect.Field field = fields[j];
                if (field.getDeclaringClass().isInterface())
                    continue;

                Object object = field.get(data);
                if (!nullObjects && object == null)
                    continue;

                // break condition
                String paramName;
                if (object instanceof java.lang.String) {
                    if (declaringClasses2 == null)
                        paramName = getName(field.getName());
                    else
                        paramName = declaringClasses2 + "."
                                + getName(field.getName());

                    // if (DEBUG) System.out.println( paramName );
                    map.put(paramName.toUpperCase(), object);
                    // if (DEBUG) System.out.println( "===> paramName=" +
                    // paramName + "<=== ===> field=" + field + "<===" );

                } else if (object instanceof java.util.Collection
                        || object instanceof javax.swing.table.TableModel) {
                    paramName = getName(field.getName());
                    int index = paramName.lastIndexOf('.');
                    if (index > 0)
                        paramName = paramName.substring(index + 1);
                    if (declaringClasses2 != null)
                        paramName = declaringClasses2 + "." + paramName;
                    map.put(paramName.toUpperCase(), object);
                    // if (DEBUG) System.err.println( "===> paramName=" +
                    // paramName + "<=== ===> field=" + field + "<===" );
                    // if (DEBUG) System.err.println( object );

                } else {
                    getFields(object, map, nullObjects, declaringClasses2,
                            level + 1);

                }

            }

        } catch (java.lang.SecurityException se) {
            throw new ReportException("java.lang.SecurityException: "
                    + se.getMessage());
        } catch (java.lang.IllegalAccessException iae) {
            throw new ReportException("java.lang.IllegalAccessException: "
                    + iae.getMessage());
        }

    }

    // remove all entry(s) where:
    // key = *.Client.* ( because there has to be key = Client.* )
    // key = *.Partner.* ( because there has to be key = Partner.* )
    // key = *.Adviser.* ( because there has to be key = Adviser.* )
    public static java.util.Map filter(final java.util.Map data,
            java.util.Collection removePatterns) {

        if (data == null || removePatterns == null
                || removePatterns.size() == 0)
            return data;

        java.util.Map filteredData = new java.util.HashMap();
        java.util.Iterator iter = data.entrySet().iterator();
        while (iter.hasNext()) {

            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            if (!findKeyToRemove(key, removePatterns))
                filteredData.put(key, value);

        }

        return filteredData;

    }

    private static boolean findKeyToRemove(String key,
            java.util.Collection removePatterns) {

        if (removePatterns == null || removePatterns.size() == 0)
            return false;

        java.util.Iterator iter2 = removePatterns.iterator();
        while (iter2.hasNext()) {
            String pattern = (String) iter2.next();
            // if ( DEBUG ) System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>> search
            // " + pattern + " in " + key );
            if (key.toUpperCase().indexOf(pattern.toUpperCase()) > 0)
                // if ( DEBUG ) System.out.println( "FOUND " + pattern + " in "
                // + key );
                return true;
        }

        return false;

    }

    public static java.util.Collection getDuplicatedPatterns() {
        java.util.Collection patterns = new java.util.Vector();
        patterns.add(".Client.");
        patterns.add(".Partner.");
        patterns.add(".Adviser.");
        return patterns;
    }

}
