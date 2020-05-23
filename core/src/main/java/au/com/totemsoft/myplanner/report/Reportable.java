/*
 * Reportable.java Created on 9 April 2003, 10:43
 */

package au.com.totemsoft.myplanner.report;

import au.com.totemsoft.myplanner.service.PersonService;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface Reportable
{

    void initializeReportData(ReportFields reportFields) throws Exception;

    void initializeReportData(ReportFields reportFields, PersonService person) throws Exception;

}