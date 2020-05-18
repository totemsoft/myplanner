/*
 * Reportable.java Created on 9 April 2003, 10:43
 */

package com.argus.financials.report;

import com.argus.financials.service.PersonService;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface Reportable
{

    void initializeReportData(ReportFields reportFields) throws Exception;

    void initializeReportData(ReportFields reportFields, PersonService person) throws Exception;

}