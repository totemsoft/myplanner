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

public class ReferenceDataLoader
// extends Thread
        implements Runnable {

    public ReferenceDataLoader() {
        // System.out.println( "TitleCode before..." );
        new TitleCode(); // <--- first one to load (required for user login)
        // System.out.println( "TitleCode after." );

        // System.out.println( "CountryCode before..." );
        new CountryCode(); // <--- second one to load (required for user login)
        // System.out.println( "CountryCode after." );
    }

    private void delay(long ms) {
        try {
            Thread.currentThread().sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public void run() {

        // System.out.println( "AdviserTypeCode before..." );
        new AdviserTypeCode();
        // System.out.println( "AdviserTypeCode after." );

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
