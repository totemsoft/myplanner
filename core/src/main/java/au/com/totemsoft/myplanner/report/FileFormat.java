/*
 * FileConverter.java
 *
 * Created on 31 January 2003, 15:28
 */

package au.com.totemsoft.myplanner.report;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class FileFormat {

    private String className;

    private String name;

    private int saveFormat;

    private String extensions;

    /** Creates a new instance of FileFormat */
    public FileFormat(String className, String name, int saveFormat,
            String extensions) {
        this.className = className;
        this.name = name;
        this.saveFormat = saveFormat;
        this.extensions = extensions;
    }

    public String toString() {
        return getClassName();
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public int getSaveFormat() {
        return saveFormat;
    }

    public String getExtensions() {
        return extensions;
    }

    public String[] getExtensionsAsArray() {

        java.util.ArrayList list = new java.util.ArrayList();
        String tmp = "";
        for (int i = 0; i < extensions.length(); i++) {

            char c = extensions.charAt(i);

            // continue condition
            if (!Character.isWhitespace(c)) {
                tmp += c;
                continue;
            }

            list.add(tmp);
            tmp = "";

        }

        // last one
        if (tmp.length() > 0)
            list.add(tmp);

        return (String[]) list.toArray(new String[0]);

    }

}
