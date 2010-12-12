/*
 * Reportable.java
 *
 * Created on 9 April 2003, 10:43
 */

package com.argus.financials.report;

import java.io.IOException;

import com.argus.financials.service.PersonService;

/**
 * 
 * @author valeri chibaev
 */

public interface Reportable {

    void initializeReportData(ReportFields reportFields)
            throws IOException;

    void initializeReportData(ReportFields reportFields, PersonService person)
            throws IOException;

}
