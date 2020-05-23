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

package au.com.totemsoft.myplanner.report.word;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.9 $
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.TableModel;

import org.w3c.dom.Document;

import au.com.totemsoft.io.XMLUtils;
import au.com.totemsoft.myplanner.report.api.IReport;
import au.com.totemsoft.myplanner.report.api.ReportException;

abstract class WordReportBase implements IReport
{

    private static final Logger logger = Logger.getLogger(WordReportBase.class.getName());

    interface IReportConstants {

        // data to be replaced, e.g. <wr:param name="Client/Person/FirstName"/>
        public static final String PARAMETER_RANGE      = "[<]wr:param name=*[/][>]";   //[/][>] MatchWildcards
        public static final String PARAMETER_NAME_BEGIN = "[<]wr:param name=";          //MatchWildcards
        public static final String PARAMETER_NAME_END   = "[/][>]";                     //MatchWildcards

        public static final String COLLECTION = "[]/"; // eg Client/Dependents[]/DOB
        
        // logical if condition
        public static final String IF_RANGE             = "[<]wr:if test=*[<][/]wr:if[>]";
        public static final String IF_TEST              = "[<]wr:if test=*[>]";               
        public static final String IF_TEST_BEGIN        = "[<]wr:if test=";               
        public static final String IF_TEST_END          = "[>]";               
        public static final String IF_BODY_BEGIN        = "[<]wr:if test=*[>]";               
        public static final String IF_BODY_END          = "[<][/]wr:if[>]";               
        
        // {}
        public static final String LOGICAL_CONDITIONS = "<>=!";
        
        // Table Of Contents
        public static final String TOC = "TOC";

        public static final int DEFAULT_ZOOM = 100;
        
    }

    // eg Normal.dot (default)
    private String template;    

    // main report file
    private String report;
    // any additonal sub-report file(s), will be appended
    private String [] subReports;
    // saved report file
    private String savedReport;
    
    /**
     * report data (key,value) (e.g. Person/FirstName) 
     */ 
    private Map data; 
    
    
    /**
     * 
     */
    protected WordReportBase() {}

    
    /**
     * File Name will be used as document template for report (default null)
     * @return
     */
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String value) {
        if (!new File(value).exists())
            System.err.println("Template [" + value + "] could not be found.");
        template = value;
    }

    /**
     * File Name will be used for report
     * @return
     */
    public String getReport() {
        return report;
    }
    public void setReport(String value) {
        if (!new File(value).exists())
            throw new IllegalArgumentException("Report [" + value + "] could not be found.");
        report = value;
    }
    
    
    /**
     * File Name will be used for report
     * @return
     */
    public String [] getSubReport() {
        return subReports;
    }
    public void setSubReport(String [] values) {
        StringBuffer sb = null;
        for (int i = 0; values != null && i < values.length; i++) {
            String value = values[i];
            if (value != null && !new File(value).exists()) {
                if (sb == null)
                    sb = new StringBuffer();
                else
                    sb.append('\n');
                sb.append("SubReport [" + value + "] could not be found.");
            }
        }
        if (sb != null)
            throw new IllegalArgumentException(sb.toString());
        subReports = values;
    }
    
    
    /**
     * @return the savedReport
     */
    public String getSavedReport() {
        return savedReport;
    }
    /**
     * @param savedReport the savedReport to set
     */
    public void setSavedReport(String savedReport) {
        this.savedReport = savedReport;
    }


    public Map getData() {
        return data;
    }
    public void setData(Map value) {
        data = value;
    }
    
    /** @inherited */
    public void setData(Document source, boolean addRootNodeName) {
        data = XMLUtils.transfer(source, addRootNodeName);
    }

    public void run() {
        
        try {
            // TODO: check ALL required properties
            doReport();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    protected abstract void doReport() throws ReportException;
    
    /////////////////////////////////////////////////////////////////////////
    // helper methods
    ////////////////////////////////////////////////////////////////////////
    protected void checkFile( String fileName ) throws ReportException {

        java.io.File file = new java.io.File( fileName );
        if ( !file.exists() ) {
            throw new ReportException( "File [" + fileName + "] does not exists" );
        }
        
    }

    protected java.lang.Object getValue( String fieldName ) {
        
        if ( fieldName == null ) 
            return null;
        
        if ( data.containsKey( fieldName ) )
            return data.get( fieldName );

        fieldName = fieldName.toUpperCase();
        if ( data.containsKey( fieldName ) )
            return data.get( fieldName );
        
        return null;
        
    }
    
    protected Collection getCollection( String collectionName ) {
        
        if ( collectionName == null ) 
            return null;
        
        List result = new ArrayList();
        Iterator iter = data.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            if (key.startsWith(collectionName)) {
                //Client/Dependents/Dependent[1]/Age=7
                // retreive ZERO based index
                int index = Integer.parseInt( key.substring( key.indexOf('[') + 1, key.indexOf(']') ) ) - 1;
                
                Map item = result.size() > index ? (Map) result.get(index) : null;
                if (item == null) {
                    while (result.size() <= index)
                        result.add(null);
                    item = new java.util.Hashtable();
                    result.set(index, item);
                }
                item.put(key.substring(key.indexOf("]/") + 2), entry.getValue());
                //System.err.println(index + "). " + key + ", " + key.substring(key.indexOf(']') + 1) + "=" + entry.getValue());
            }
        }
        
        return result;
        
    }
    
    protected Collection getCollection(String collectionName, TableModel tm ) {
        
        String [] columnNames = new String [tm.getColumnCount()];
        for (int c = 0; c < tm.getColumnCount(); c++)
            columnNames[c] = tm.getColumnName(c);
        
        List result = new ArrayList();
        for (int r = 0; r < tm.getRowCount(); r ++) {
            Map item = new Hashtable();
            for (int c = 0; c < tm.getColumnCount(); c++) {
                //Client/Dependents/Dependent[1]/Age=7
                String [] tmp = columnNames[c].split(" ");
                String property = "";
                for (int i = 0; i < tmp.length; i++) {
                    String s = tmp[i].trim();
                    if (s.length() > 1)
                        property += Character.toUpperCase(s.charAt(0)) + s.substring(1);
                }
                item.put(property, tm.getValueAt(r, c));
            }
            result.add(item);
        }
        
        return result;
        
    }
    
    protected boolean evaluateExpression( String expression ) {
        //logger.info( "expression: " + expression );
        
        if ( expression == null )
            return false;
        
        String condition = "";
        for ( int i = 0; i < expression.length(); i++ ) {
            char c = expression.charAt(i);
            if ( IReportConstants.LOGICAL_CONDITIONS.indexOf(c) >= 0 ) // "<>="
                condition += c;
        }
        condition = condition.trim();

        int index = expression.indexOf(condition);
        if (index < 0)
            return false;
        
        String value1 = expression.substring(0, index).trim();
        String value2 = expression.substring(index + condition.length()).trim();
        
        //logger.info( "reconstructed expression: " + value1 + condition + value2 );
        
        //value1.compareToIgnoreCase( value );
        if ( condition.equals( ">" ) )
            return compare( value1, value2 ) > 0;
        if ( condition.equals( ">=" ) )
            return compare( value1, value2 ) >= 0;
        if ( condition.equals( "<" ) )
            return compare( value1, value2 ) < 0;
        if ( condition.equals( "<=" ) )
            return compare( value1, value2 ) <= 0;
        if ( condition.equals( "=" ) || condition.equals( "==" ) )
            return compare( value1, value2 ) == 0;
        if ( condition.equals( "<>" ) || condition.equals( "!=" ) )
            return compare( value1, value2 ) != 0;
        
        logger.warning( "FAILED to evaluate expression: [" + value1 + condition + value2 + "]");
        return false;
        
    }
    
    private int compare(String s1, String s2) {
        if ( s1 == null && s2 == null ) return 0;
        if ( s1 == null ) return -1;
        if ( s2 == null ) return 1;

        // finally try to compare as string
        int result = s1.compareToIgnoreCase(s2);
        if (result < 0) return -1;
        if (result > 0) return 1;
        return 0;            
        
    }

//    private int compare(double d1, double d2) {
//        if (d1 < d2) return -1;
//        if (d1 > d2) return 1;
//        return 0;
//    }
//
//    private int compare(Number n1, Number n2) {
//        if ( n1 == null && n2 == null ) return 0;
//        if ( n1 == null ) return -1;
//        if ( n2 == null ) return 1;
//
//        return compare( n1.doubleValue(), n2.doubleValue() );
//    }

    
    protected java.util.Map getParameter(
            java.lang.String paramName,
            java.util.Collection collection ) 
        throws ReportException 
    {
        java.util.Map map = new java.util.TreeMap();
        
        int count = 0;
        java.util.Iterator iter = collection.iterator();
        while ( iter.hasNext() ) {
            count++;
            
            java.util.Map data = (java.util.Map) iter.next(); // collection item
            
            java.util.Iterator iter2 = data.entrySet().iterator();
            while ( iter2.hasNext() ) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iter2.next();
                
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                
                String param = paramName + "[" + count + "]/" + key; // XXX[1]/YYY
                map.put( param, value );
                //logger.info( param + "=" + value );
                
            }
            
        }
        
        return map;
        
    }

    
    /////////////////////////////////////////////////////////////////////////
    // TODO: move to utility class
    ////////////////////////////////////////////////////////////////////////
    protected static TableModel convertToTableModel( Collection rows ) throws ReportException {
        
        Vector rowData = new Vector( rows.size() );
        Vector columnNames = null;
        
        Iterator iter = rows.iterator();
        while ( iter.hasNext() ) {
            Object item = iter.next(); // Item
            
            try {
                java.lang.reflect.Field [] fields = item.getClass().getFields();
                
                // init column names
                if ( columnNames == null ) {
                    columnNames = new Vector();
                    
                    for ( int i = 0; i < fields.length; i++ ) {
                        java.lang.reflect.Field field = fields[i];
                        columnNames.add( field.getName() );
                    }
                    
                }
                
                // init row data
                Vector row = new Vector( fields.length );
                for ( int i = 0; i < fields.length; i++ ) {
                    
                    java.lang.reflect.Field field = fields[i];
                    
                    Object object = field.get( item );
                    if ( object == null )
                        object = "";
                    
                    row.add( object );
                    
                }
                
                rowData.add( row );
                
            } catch ( SecurityException ex ) {
                throw new ReportException( "java.lang.SecurityException: " + ex.getMessage() );
            } catch ( IllegalAccessException ex ) {
                throw new ReportException( "java.lang.IllegalAccessException: " + ex.getMessage() );
            }
            
        }
        
        return new javax.swing.table.DefaultTableModel( rowData, columnNames );
        
    }


}
