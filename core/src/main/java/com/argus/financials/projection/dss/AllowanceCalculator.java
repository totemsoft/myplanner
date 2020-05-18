/*
 * AllowanceCalculator.java
 *
 * Created on May 22, 2003, 12:21 PM
 */

package com.argus.financials.projection.dss;

/**
 * 
 * @author shibaevv
 */
public class AllowanceCalculator extends DSSCalculator implements DSSConstants {

    /** Creates a new instance of AllowanceCalculator */
    public AllowanceCalculator(int personType) {
        this.personType = personType;
    }

    public void maxBenefit() {
        maxBenefit = tc.getDSSParams().maxBenefit(NEW_START_ALLOWANCE);
        tc.setResult(personType, MAX_BENEFIT, maxBenefit);

    }

    public void assetTest() {

        double totalAsset = 0;

        if (tc.getPersonInfo().isPartnered())
            totalAsset = tc.getTotalTestAsset(COMBINED);
        else
            totalAsset = tc.getTotalTestAsset(CLIENT);

        assetThreshold = tc.getDSSParams().getAssetThreshold();
        double assetRate = tc.getDSSParams().getAssetRate();

        assetTestResult = totalAsset <= assetThreshold ? maxBenefit : 0.;
        tc.setResult(personType, ASSET_TEST_RESULT, assetTestResult);
        tc.setResult(personType, ASSET_THRESHOLD, assetThreshold);
    }

    public void incomeTest() {

        double incomeFornightlyC = tc.getTotalTestIncome(CLIENT) / 26;
        double incomeFornightlyP = tc.getTotalTestIncome(PARTNER) / 26;
        double incomeFornightlyCom = tc.getTotalTestIncome(COMBINED) / 26;
        if (tc.getCalcModel() == A_N) {
            double income = (personType == CLIENT) ? incomeFornightlyC
                    : incomeFornightlyP;
            double incomeP = (personType == CLIENT) ? incomeFornightlyP
                    : incomeFornightlyC;
            incomeTestResult = results(income) - partnerResults(incomeP);
        } else if (tc.getCalcModel() == A_A) {
            double income = (personType == CLIENT) ? incomeFornightlyC
                    : incomeFornightlyP;
            incomeTestResult = results(income);
        } else if (tc.getCalcModel() == A_P) {
            double income = incomeFornightlyCom / 2;
            incomeTestResult = results(income);
        }

        incomeTestResult = incomeTestResult < 0 ? 0 : incomeTestResult;
        tc.setResult(personType, INCOME_TEST_RESULT, incomeTestResult);
        tc.setResult(personType, INCOME_THRESHOLD, incomeThreshold);
    }

    public double results(double income) {
        incomeThreshold = ALLOWANCE_LOW_THRESHOLD;

        if (income <= ALLOWANCE_LOW_THRESHOLD)
            return maxBenefit;
        if (income > ALLOWANCE_LOW_THRESHOLD
                && income <= ALLOWANCE_HIGH_THRESHOLD)
            return maxBenefit - (income - ALLOWANCE_LOW_THRESHOLD)
                    * ALLOWANCE_L_RDCTN_FCTR;

        return maxBenefit
                - ((income - ALLOWANCE_HIGH_THRESHOLD) * ALLOWANCE_H_RDCTN_FCTR + (ALLOWANCE_HIGH_THRESHOLD - ALLOWANCE_LOW_THRESHOLD)
                        * ALLOWANCE_L_RDCTN_FCTR);

    }

    public double partnerResults(double income) {
        if (income > ALLOWANCE_PARTNER_THRESHOLD)
            return (income - ALLOWANCE_PARTNER_THRESHOLD)
                    * ALLOWANCE_H_RDCTN_FCTR;
        return 0;
    }

}
