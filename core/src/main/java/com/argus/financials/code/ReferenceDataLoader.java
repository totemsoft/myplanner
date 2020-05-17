/*
 * ReferenceDataLoader.java
 *
 * Created on 29 October 2002, 14:36
 */

package com.argus.financials.code;

import org.apache.log4j.Logger;

/**
 * 
 * @author valeri chibaev
 */

public class ReferenceDataLoader {

    /** Logger. */
    private final static Logger LOG = Logger.getLogger(ReferenceDataLoader.class);

    public ReferenceDataLoader()
    {
        new TitleCode(); // <--- first one to load (required for user login)
        new CountryCode(); // <--- second one to load (required for user login)
        new AdviserTypeCode();
        // new SexCode();
        // new MaritalCode();
        // new StateCode( null );
        // new SuburbPostCode( null );
        //new FinancialType();
        new FundType();
        new Institution();
        new FrequencyCode();
        new IndustryCode();
        new InvestmentType();
        new OccupationCode();
        new PeriodCode();
        new SourceCode();
        new FinancialServiceCode();
        LOG.info("ReferenceDataLoader finished!");
    }

}