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

package com.argus.financials.myplanner.report.word;

import com.argus.financials.myplanner.report.ReportException;

public class WordReportRtf 
    extends WordReportBase 
    implements Runnable
{

    /**
     * 
     */
    public WordReportRtf() {
        super();
    }

    
    public void run() {
    
        try {
            super.run();
        } catch (Exception ex) {
            // TODO: exception handling
            throw new RuntimeException(ex);
        }

    }


    @Override
    protected void doReport() throws ReportException {
        // TODO Auto-generated method stub
        
    }

}
