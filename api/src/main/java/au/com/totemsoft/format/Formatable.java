/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.format;

/**
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1 $
 */

public interface Formatable {

    public boolean isNumber( String value );
    public boolean isFormatedNumber( String value );
    
    public double doubleValue( String value );
    public double doubleValue( javax.swing.text.JTextComponent tc );

    public Double getDoubleValue( String value );
    public Double getDoubleValue( javax.swing.text.JTextComponent tc );
    
    public java.math.BigDecimal getBigDecimalValue( String value );
    //public java.math.BigDecimal getBigDecimalValue( javax.swing.text.JTextComponent tc );
    
    public String toString( double value );
    public String toString( java.lang.Number value );
    
}

