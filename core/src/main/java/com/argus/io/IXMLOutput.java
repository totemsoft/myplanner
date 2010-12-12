/*
 * IXMLOutput.java
 *
 * Created on 25 May 2004, 16:57
 */

package com.argus.io;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author  Administrator
 */

public interface IXMLOutput {
    
    /**
     *  Create xml element.
     *  Parameters: elementName - element name
     */
    public void toXML(Writer result) throws IOException;
    
    public void toXML(String elementName, Writer result) throws IOException;
}
