/*
 * AbstractCode.java
 *
 * Created on 31 July 2001, 15:02
 */

package com.argus.financials.code;

import java.io.Serializable;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.service.ServiceAware;

/*
 * In Swing 1.1.1 and later, you can place HTML in your labels to get a
 * multi-line label, as in
 * 
 * String htmlLabel = "<html><sup>HTML</sup> <sub><em>Label</em></sub><br>" + "<font
 * color=\"#FF0080\"><u>Multi-line</u></font>"; JLabel label = new
 * JLabel(htmlLabel);
 * 
 * If you want to be able to use \n and \r to indicate new lines, you would need
 * to define your own UI for the Swing component.
 * 
 */

public abstract class Code
    extends ServiceAware
    implements Serializable//, ICode
{

    public final static String NONE = "";

    public final static Integer VALUE_NONE = null;

    public final static String[] EMPTY_KEYS = new String[] { Code.NONE };

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    protected Map getCodeMap() {
        return null;
    }

    public Collection getKeys() {
        return getCodeMap().keySet();
    }

    public Collection getValues() {
        return getCodeMap().values();
    }

    public boolean isValidCodeID(Integer codeID) {
        return (codeID == null) ? true : getCodeMap().containsValue(codeID);
    }

    public boolean isValidCodeDesc(String codeDesc) {
        if (codeDesc == null)
            return true;

        // TODO: case insensitive ???
        return getCodeMap().containsKey(codeDesc.trim());
    }

    public boolean isValidThrow(Integer codeID) throws InvalidCodeException {
        if (isValidCodeID(codeID))
            return true;
        else
            throw new InvalidCodeException("Invalid CodeID: " + codeID);
    }

    public Integer getCodeID(String codeDesc) {
        if (codeDesc == null)
            return VALUE_NONE;

        codeDesc = codeDesc.trim();

        // TODO: case insensitive ???
        if (isValidCodeDesc(codeDesc))
            return (Integer) getCodeMap().get(codeDesc);

        return VALUE_NONE;
    }

    public String getCodeDescription(Long codeID) {
        return codeID == null ? NONE : getCodeDescription(codeID.intValue());
    }
    public String getCodeDescription(Integer codeID) {
        // if ( ( codeID == null ) || ( codeID.equals( new Integer(0) ) ) )
        if (codeID == null)
            return NONE;

        Iterator iter = getCodeMap().entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();

            Integer value = (Integer) e.getValue();

            if (value == null)
                continue;

            if (value.equals(codeID))
                return (String) e.getKey();
        }

        return NONE;
    }

    public Object[] getCodeIDs() {
        Collection collection = getCodeMap().values();
        return (new HashSet(collection)).toArray();
    }

    public String[] getCodeDescriptions() {
        Set<String> set = getCodeMap().keySet();
        return (String[]) set.toArray(new String[0]);
    }

}