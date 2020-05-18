/*
 * DSSCalculator.java
 *
 * Created on April 29, 2003, 7:51 PM
 */

package com.argus.financials.projection.dss;

import com.argus.financials.projection.DocumentNames;

/**
 * 
 * @author shibaevv
 */
public abstract class DSSCalculator implements DocumentNames {

    int personType;

    DSSContainer tc;

    double assetTestResult;

    double incomeTestResult;

    double basicBenefit;

    double maxBenefit;

    double incomeThreshold;

    double assetThreshold;

    public abstract void maxBenefit();

    public abstract void assetTest();

    public abstract void incomeTest();

    public void basicBenefit() {
        basicBenefit = Math.min(assetTestResult, incomeTestResult);
        tc.setResult(personType, BASIC_BENEFIT, basicBenefit);
    }

    public void calc() {
        maxBenefit();
        assetTest();
        incomeTest();
        basicBenefit();
    }

    public void register(DSSContainer tc) {
        this.tc = tc;
    }
}
