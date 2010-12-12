/*
 * TaxRule.java
 *
 * Created on 4 February 2002, 23:57
 */

package com.argus.financials.tax.au;

/**
 * 
 * @author Administrator
 * @version
 */

abstract class TaxRule {

    TaxContainer tc;

    abstract void apply();

    void register(TaxContainer tc) {
        this.tc = tc;
    }

}
