/*
 * TaxRule.java
 *
 * Created on 4 February 2002, 23:57
 */

package au.com.totemsoft.myplanner.tax.au;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

abstract class TaxRule {

    TaxContainer tc;

    abstract void apply();

    void register(TaxContainer tc) {
        this.tc = tc;
    }

}
