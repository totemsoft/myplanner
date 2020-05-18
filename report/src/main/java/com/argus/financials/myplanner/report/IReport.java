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

package com.argus.financials.myplanner.report;

import java.util.Map;

import org.w3c.dom.Document;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.5 $
 */

public interface IReport /*extends com.argus.activex.wordreport.IWordReport*/ {

    /**
     * File Name will be used as document template for report
     * (default null, Normal.dot will be used)
     * @return
     */
    public String getTemplate();
    public void setTemplate(String value);

    /**
     * File Name will be used for report
     * @return
     */
    public String getReport();
    public void setReport(String value);
    
    /**
     * Additional File Name(s) will be used for report
     * @return
     */
    public String [] getSubReport();
    public void setSubReport(String [] values);
    
    /**
     * @return the savedReport
     */
    public String getSavedReport();
    public void setSavedReport(String savedReport);

    public Map getData();
    public void setData(Map value);

    /**
     * Use xml document to set report data.
     * @param source The xml document.
     * @param addRootNodeName Control whether Root Node Name will be added to generated xpath.
     */
    public void setData(Document source, boolean addRootNodeName);
    
    /**
     * Generate report.
     */
    public void run();

}