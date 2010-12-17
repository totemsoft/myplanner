/*
 * ReferenceDataLoader.java
 *
 * Created on 29 October 2002, 14:36
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 */

public class ReferenceDataLoader implements Runnable {

    public ReferenceDataLoader() {
        new TitleCode(); // <--- first one to load (required for user login)
        new CountryCode(); // <--- second one to load (required for user login)
    }

    public void run() {
        new AdviserTypeCode();
        // new SexCode();
        // new MaritalCode();
        // new StateCode( null );
        // new SuburbPostCode( null );
        new FinancialType();
        new FundType();
        new Institution();
        new FrequencyCode();
        new IndustryCode();
        new InvestmentType();
        new OccupationCode();
        new PeriodCode();
        new SourceCode();
        new FinancialServiceCode();
        System.out.println("ReferenceDataLoader finished!");
    }

}