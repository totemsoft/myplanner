/*
 * PensionCalculator.java
 *
 * Created on April 29, 2003, 8:00 PM
 */

package au.com.totemsoft.myplanner.projection.dss;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class PensionCalculator extends DSSCalculator {

    /** Creates a new instance of PensionCalculator */
    public PensionCalculator(int personType) {
        this.personType = personType;
    }

    public void maxBenefit() {
        maxBenefit = tc.getDSSParams().maxBenefit(AGE_PENSION);
        tc.setResult(personType, MAX_BENEFIT, maxBenefit);

    }

    public void incomeTest() {
        double incomeFornightly = 0;
        int divident = 1;
        if (tc.getPersonInfo().isPartnered()) {
            incomeFornightly = tc.getTotalTestIncome(COMBINED) / 26;
            divident = 2;
        } else
            incomeFornightly = tc.getTotalTestIncome(CLIENT) / 26;

        incomeThreshold = tc.getDSSParams().getPensionIncomeThreshold();
        double incomeRate = tc.getDSSParams().getPensionIncomeRate();

        incomeTestResult = incomeFornightly <= incomeThreshold ? maxBenefit
                : maxBenefit
                        - ((incomeFornightly - incomeThreshold) * incomeRate)
                        / divident;

        incomeTestResult = incomeTestResult < 0 ? 0 : incomeTestResult;
        tc.setResult(personType, INCOME_TEST_RESULT, incomeTestResult);
        tc.setResult(personType, INCOME_THRESHOLD, incomeThreshold);
    }

    public void assetTest() {
        double totalAsset = 0;
        int divident = 1;

        if (tc.getPersonInfo().isPartnered()) {
            totalAsset = tc.getTotalTestAsset(COMBINED);
            divident = 2;
        } else
            totalAsset = tc.getTotalTestAsset(CLIENT);

        assetThreshold = tc.getDSSParams().getAssetThreshold();
        double assetRate = tc.getDSSParams().getAssetRate();

        assetTestResult = totalAsset <= assetThreshold ? maxBenefit
                : maxBenefit
                        - (((totalAsset - assetThreshold) / 1000) * assetRate)
                        / divident;
        assetTestResult = assetTestResult < 0 ? 0 : assetTestResult;
        tc.setResult(personType, ASSET_TEST_RESULT, assetTestResult);
        tc.setResult(personType, ASSET_THRESHOLD, assetThreshold);
    }

}
