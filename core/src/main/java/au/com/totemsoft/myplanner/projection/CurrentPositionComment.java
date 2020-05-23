/*
 * SnapshotComment.java
 *
 * Created on 4 October 2001, 12:09
 */

package au.com.totemsoft.myplanner.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.text.MessageFormat;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.Number2;

public class CurrentPositionComment {

    private static double DEVIATION = 0.0; // +/- 1%

    private static MessageFormat[] commentFormat = new MessageFormat[] {
    // Quick View comments
            // compalsory message
            new MessageFormat("This capital will last till age {0}."),
            // compalsory message
            new MessageFormat("Life expectancy is age {0}."),
            // ClientView Actual Income = Reqired Income
            new MessageFormat(
                    "The clients are able to meet their retirement objective based on the information entered."),
            // ClientView Actual Income > Reqired Income
            new MessageFormat(
                    "The clients are able to meet their retirement objective and have excess assets of {0} (Residual Assets) based on the information entered."),
            // ClientView Actual Income < Reqired Income
            new MessageFormat(
                    "The clients are not able to meet their retirement objective based on the information entered. Clients need more savings/capital"
                            +
                            // " ( e.g. superannuation contributions of {0} p.a.
                            // are required to achieve the goal)" +
                            "."),

    };

    /** Creates new SnapshotComment */
    public CurrentPositionComment() {
    }

    public static String getComment(CurrentPositionCalc snapshotCalc) {

        Number2 number = Number2.getNumberInstance();
        Currency currency = Currency.getCurrencyInstance();

        AssetSpend assetSpend = snapshotCalc.getAssetSpend();
        double targetAge = snapshotCalc.getTargetAge();
        double actualYears = assetSpend.getActualYears();
        int yearsSpend = snapshotCalc.getYearsIntSpend();
        double lifeExpectancyAfterRetire = snapshotCalc
                .getLifeExpectancyAfterRetire();

        String message = (actualYears > yearsSpend) ? ""
                : commentFormat[0].format(new Object[] { number
                        .toString((int) (targetAge + actualYears)) })
                        + commentFormat[1]
                                .format(new Object[] { number
                                        .toString((int) (targetAge + lifeExpectancyAfterRetire)) });

        double requiredIncome = assetSpend.getSpendValue();
        if (requiredIncome <= 0)
            return message;

        double actualIncome = assetSpend.getActualIncome(false);
        if (actualIncome == 0)
            actualIncome = assetSpend.getActualIncome(true);

        double diff = (actualIncome - requiredIncome) / requiredIncome;

        if (Math.abs(diff) <= DEVIATION)
            message += commentFormat[2].format(null);

        else if (diff > DEVIATION) {
            double residualValue = assetSpend.getTargetValue();// getResidualValue();
            message += commentFormat[3].format(new Object[] { currency
                    .toString(residualValue) });

        } else if (diff < -DEVIATION) {
            /*
             * AssetGrowthLinked superAsset = snapshotCalc.getAsset(
             * AssetCode.SUPER );
             * 
             * double targetValue = superAsset.getTargetValue(); double
             * optimalInitialValue = assetSpend.getOptimalInitialValue(true);
             * double totalGrowthValue = snapshotCalc.getTotalTargetValue();
             * 
             * double optimalTargetValue = targetValue + optimalInitialValue -
             * totalGrowthValue;
             * 
             * double requiredContributions =
             * superAsset.getOptimalContributions( true, optimalTargetValue );
             */

            message += commentFormat[4].format(null
            // new Object [] { currency.toString( requiredContributions ) }
                    );

        }

        return message;

    }

    private static MessageFormat[] allocatedPensionComment = new MessageFormat[] {
    // Allocated pension View comment
    new MessageFormat(
            "Based on the information entered and the investments strategy selected, we project that the allocated pension assets will provide the desired income until the client''s {0} birthday. "),

    };

    public static String getComment(AllocatedPensionCalc apCalc) {

        return allocatedPensionComment[0].format(new Object[] { apCalc
                .getActualYears() });

    }

}
