/*
 * StrategyType.java
 *
 * Created on July 9, 2002, 12:35 PM
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Collection;
import java.util.TreeSet;

import au.com.totemsoft.myplanner.api.code.CodeComparator;
import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.util.ReferenceCode;

public class FinancialClass extends BaseCode implements FinancialClassID {

    private static Collection codes;

    public Collection getCodes() {
        if (codes == null) {
            codes = new TreeSet(new CodeComparator());

            codes.add(RC_ASSET_CASH);
            codes.add(RC_ASSET_INVESTMENT);
            codes.add(RC_ASSET_PERSONAL);
            codes.add(RC_ASSET_SUPERANNUATION);
            codes.add(RC_INCOME_STREAM);

            codes.add(RC_LIABILITY);

            codes.add(RC_REGULAR_INCOME);
            codes.add(RC_REGULAR_EXPENSE);
            codes.add(RC_TAX_OFFSET);
        }
        return codes;
    }

    public static ReferenceCode getStrategyCode(Integer objectTypeID) {

        if (objectTypeID == null)
            return null;

        switch (objectTypeID.intValue()) {
        case (iASSET_CASH):
            return RC_ASSET_CASH;
        case (iASSET_INVESTMENT):
            return RC_ASSET_INVESTMENT;
        case (iASSET_PERSONAL):
            return RC_ASSET_PERSONAL;
        case (iASSET_SUPERANNUATION):
            return RC_ASSET_SUPERANNUATION;
        case (iASSET_INCOME_STREAM):
            return RC_INCOME_STREAM;
        case (iLIABILITY):
            return RC_LIABILITY;

        case (iREGULAR_INCOME):
            return RC_REGULAR_INCOME;
        case (iREGULAR_EXPENSE):
            return RC_REGULAR_EXPENSE;
        case (iTAX_OFFSET):
            return RC_TAX_OFFSET;
        default:
            return null;
        }

    }

}
