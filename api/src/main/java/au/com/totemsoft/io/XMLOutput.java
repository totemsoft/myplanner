/*
 * XMLOutput.java
 *
 * Created on 24 May 2004, 18:57
 */

package au.com.totemsoft.io;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public abstract class XMLOutput implements IXMLOutput {
    
	protected static long LONG_NV = Long.MIN_VALUE;
	protected static int INT_NV = Integer.MIN_VALUE;
	protected static short SHORT_NV = Short.MIN_VALUE;
	
	
    public void toXML(Writer result) throws IOException {
    	toXML(getClassShortName(), result);
    }
    
    public abstract void toXML(String elementName, Writer result) throws IOException;

    public static String toXML(String elementName, String value) {
        return "<" + elementName + ">" + value + "</" + elementName + ">";
    }

    public static String toXML(String elementName, long value) {
        return "<" + elementName + ">" + value + "</" + elementName + ">";
    }

    public static String toXML(String elementName, int value) {
        return "<" + elementName + ">" + value + "</" + elementName + ">";
    }

    public static String toXML(String elementName, Object value) {
        return toXML( elementName, value.toString() );
    }

    public String getClassShortName() {
    	return getClassShortName(getClass().getName());
    }
    public static String getClassShortName( String className ) {
        int index = className.lastIndexOf('.');
        if (index >= 0)
            className = className.substring( index + 1 );
        
        return className;
    }

    protected static String toHexString(short value) {
    	return "0x" + Integer.toHexString( value );
    }

    protected static String toHexString(int value) {
    	return "0x" + Integer.toHexString( value );
    }

    protected static String toHexString(long value) {
    	return "0x" + Long.toHexString( value );
    }

}
