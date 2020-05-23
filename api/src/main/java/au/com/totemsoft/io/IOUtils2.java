package au.com.totemsoft.io;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 
 */

import java.awt.Component;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class IOUtils2 {

    public static final String ENCODING = "ISO-8859-1";

    public static final String ENCODING_2_SERIALIZE = "ISO8859_1";

    /** Creates new */
    private IOUtils2() {
    }

    /**
     * READ
     */
    public static void read(Component component, String fileName) {

        java.io.FileReader fr = null;
        java.io.BufferedReader input = null;

        try {
            fr = new java.io.FileReader(fileName);
            input = new java.io.BufferedReader(fr);

            read(component, input);

        } catch (java.io.IOException ioe) {
            /* ignore by now */
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (java.io.IOException ioe) { /* ignore by now */
                }
            }

            if (fr != null) {
                try {
                    fr.close();
                } catch (java.io.IOException ioe) { /* ignore by now */
                }
            }

        }

    }

    public static void read(Component component, BufferedReader input)
            throws IOException {

        java.util.Properties properties = new java.util.Properties();
        String name = input.readLine();

        while (name != null) {
            properties.put(name, input.readLine());
            name = input.readLine();
        }

        read(component, properties);

    }

    private static void read(Component component,
            java.util.Properties properties) {

        if (component.getAccessibleContext() == null) {
            // System.out.println( "!!!!!!!!!!!!!!!!!!" + component ); // e.g.
            // JCChart
            return;
        }

        String name = component.getAccessibleContext().getAccessibleName();

        if (name != null && properties.containsKey(name)) {

            String value = properties.getProperty(name);

            if (component instanceof JTextComponent) {
                if (component instanceof JTextField)
                    ((JTextField) component).setText(value);
                else if (component instanceof JTextArea)
                    ((JTextArea) component).setText(value);
                else if (component instanceof JEditorPane) {
                    if (component instanceof JTextPane)
                        ((JTextPane) component).setText(value);
                    else
                        ((JEditorPane) component).setText(value);
                }
            } else if (component instanceof AbstractButton) {
                ((AbstractButton) component).setSelected(new Boolean(value)
                        .booleanValue());
            } else if (component instanceof JComboBox) { // consists of many
                                                            // other UI, Render,
                                                            // ...
                ((JComboBox) component).setSelectedItem(value);
            }
        } else {
            if (!(component instanceof Container))
                return;

            Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++)
                read(container.getComponent(i), properties);
        }

    }

    /**
     * WRITE
     */
    public static void write(Component component, String fileName) {

        java.io.FileWriter fw = null;
        java.io.BufferedWriter output = null;

        try {
            fw = new java.io.FileWriter(fileName);
            output = new java.io.BufferedWriter(fw);

            writeHeader(component, output);

            write(component, output);
        } catch (java.io.IOException ioe) {
            /* ignore by now */
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (java.io.IOException ioe) { /* ignore by now */
                }
            }

            if (fw != null) {
                try {
                    fw.close();
                } catch (java.io.IOException ioe) { /* ignore by now */
                }
            }

        }

    }

    public static void writeHeader(Component component, BufferedWriter output)
            throws IOException {
        output.write("# " + new java.util.Date() + '\n');
        output.write("# " + component.getClass().getName() + '\n');
    }

    public static void write(Component component, BufferedWriter output)
            throws IOException {

        String value = null;

        if (component instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) component;
            value = tc.getText();

        } else if (component instanceof AbstractButton) {
            if (component instanceof JButton)
                return;

            boolean selected = ((AbstractButton) component).isSelected();
            value = selected ? Boolean.TRUE.toString() : Boolean.FALSE
                    .toString();

        } else if (component instanceof JComboBox) { // consists of many
                                                        // other UI, Render, ...
            Object selectedItem = ((JComboBox) component).getSelectedItem();
            value = selectedItem == null ? null : selectedItem.toString();

        } else {
            if (!(component instanceof Container))
                return;

            Container container = (Container) component;
            for (int i = 0; i < container.getComponentCount(); i++)
                write(container.getComponent(i), output);

        }

        if (value == null)
            return;

        String name = component.getAccessibleContext().getAccessibleName();
        if (name == null)
            return;

        output.write(name + '\n');
        output.write(value + '\n');

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void writeObject(Object obj, String filename)
            throws IOException {
        writeObject(obj, new File(filename));
    }

    public static void writeObject(Object obj, File file) throws IOException {

        OutputStream os = new FileOutputStream(file);
        try {
            writeObject(obj, os);
        } finally {
            os.close();
        }

    }

    public static void writeObject(Object obj, OutputStream os)
            throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(os);
        try {
            out.writeObject(obj);
        } finally {
            out.close();
        }

    }

    public static Object readObject(String filename) throws IOException,
            ClassNotFoundException {
        return readObject(new File(filename));
    }

    public static Object readObject(File file) throws IOException,
            ClassNotFoundException {

        InputStream is = new FileInputStream(file);
        try {
            return readObject(is);
        } finally {
            is.close();
        }

    }

    public static Object readObject(InputStream is) throws IOException,
            ClassNotFoundException {

        ObjectInputStream in = new ObjectInputStream(is);
        try {
            return in.readObject();
        } finally {
            in.close();
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static java.util.Properties objectToProperties(Object obj) {

        if (obj == null)
            return new java.util.Properties();
        if (obj instanceof java.util.Properties)
            return (java.util.Properties) obj;

        try {
            java.util.Properties props = new java.util.Properties();
            props.load(new java.io.ByteArrayInputStream(obj.toString()
                    .getBytes()));
            return props;
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    public static String propertiesToString(java.util.Properties props) {

        java.io.OutputStream os = new java.io.ByteArrayOutputStream();
        try {
            props.store(os, "");
            return os.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return null;
        } finally {
            try {
                os.close();
                os = null;
            } catch (java.io.IOException e) {
                return null;
            }
        }

    }

    public static boolean equals(java.util.Properties prop1,
            java.util.Properties prop2) {

        java.util.Iterator iter = prop1.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            String key1 = (String) entry.getKey();
            String value1 = prop1.getProperty(key1);
            if (value1.indexOf('\\') == 0 && value1.length() > 0)
                value1 = value1.substring(1);

            String value2 = prop2.getProperty(key1);
            if (value2.indexOf('\\') == 0 && value2.length() > 0)
                value2 = value2.substring(1);

            if (!value1.equals(value2)) {
                return false;
            }
        }

        return true;

    }

}
