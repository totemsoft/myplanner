/*
 * FinancialTypeID.java
 *
 * Created on 9 November 2001, 11:22
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * financial type/code constants (see FinancialType/FinancialCode tables)
 * 
 */

import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.util.ReferenceCode;

public interface FinancialClassID {

    public static final int iASSET_CASH = ObjectTypeConstant.ASSET_CASH; // 10;

    public static final int iASSET_INVESTMENT = ObjectTypeConstant.ASSET_INVESTMENT; // 11;

    public static final int iASSET_PERSONAL = ObjectTypeConstant.ASSET_PERSONAL; // 12;

    public static final int iASSET_SUPERANNUATION = ObjectTypeConstant.ASSET_SUPERANNUATION; // 13;

    public static final int iREGULAR_INCOME = ObjectTypeConstant.REGULAR_INCOME; // 14;

    public static final int iREGULAR_EXPENSE = ObjectTypeConstant.REGULAR_EXPENSE; // 15;

    public static final int iLIABILITY = ObjectTypeConstant.LIABILITY; // 16;

    public static final int iINSURANCE = ObjectTypeConstant.INSURANCE; // 17;

    public static final int iTAX_OFFSET = ObjectTypeConstant.TAX_OFFSET; // 18;

    public static final int iASSET_INCOME_STREAM = ObjectTypeConstant.INCOME_STREAM; // 19

    // public static final int ANTICIPATED_PAYMENT =
    // ObjectTypeID.ANTICIPATED_PAYMENT; //18;
    // public static final int ANTICIPATED_TRANSFER =
    // ObjectTypeID.ANTICIPATED_TRANSFER; //19;
    public static final int iFINANCIAL = ObjectTypeConstant.FINANCIAL; // 20;

    public static final int iASSET = ObjectTypeConstant.ASSET; // 21;

    public static final int iREGULAR = ObjectTypeConstant.REGULAR; // 22;

    // public static final int ANTICIPATED = ObjectTypeID.ANTICIPATED; //23;

    public final static Integer ASSET_CASH = new Integer(iASSET_CASH);

    public final static Integer ASSET_SUPERANNUATION = new Integer(
            iASSET_SUPERANNUATION);

    public final static Integer ASSET_INVESTMENT = new Integer(
            iASSET_INVESTMENT);

    public final static Integer ASSET_PERSONAL = new Integer(iASSET_PERSONAL);

    public final static Integer ASSET_INCOME_STREAM = new Integer(
            iASSET_INCOME_STREAM);

    public final static Integer LIABILITY = new Integer(iLIABILITY);

    public final static Integer REGULAR_INCOME = new Integer(iREGULAR_INCOME);

    public final static Integer REGULAR_EXPENSE = new Integer(iREGULAR_EXPENSE);

    public final static Integer TAX_OFFSET = new Integer(iTAX_OFFSET);

    public static final int[] iASSETS = new int[] { iASSET_CASH,
            iASSET_INVESTMENT, iASSET_PERSONAL, iASSET_SUPERANNUATION,
            iASSET_INCOME_STREAM };

    public static final Integer[] ASSETS = new Integer[] { ASSET_CASH,
            ASSET_INVESTMENT, ASSET_PERSONAL, ASSET_SUPERANNUATION,
            ASSET_INCOME_STREAM };

    public static final int[] iREGULARS = new int[] { iREGULAR_INCOME,
            iREGULAR_EXPENSE, iTAX_OFFSET };

    public static final Integer[] REGULARS = new Integer[] { REGULAR_INCOME,
            REGULAR_EXPENSE, TAX_OFFSET };

    public static final ReferenceCode RC_ASSETS_LIABILITIES = new ReferenceCode(
            -1, "Assets and Liabilities"); // group

    public static final ReferenceCode RC_ASSET_CASH = new ReferenceCode(
            iASSET_CASH, "Cash Assets");

    public static final ReferenceCode RC_ASSET_INVESTMENT = new ReferenceCode(
            iASSET_INVESTMENT, "Investment Assets");

    public static final ReferenceCode RC_ASSET_PERSONAL = new ReferenceCode(
            iASSET_PERSONAL, "Physical/Personal Assets");

    public static final ReferenceCode RC_ASSET_SUPERANNUATION = new ReferenceCode(
            iASSET_SUPERANNUATION, "Superannuation Funds");

    public static final ReferenceCode RC_INCOME_STREAM = new ReferenceCode(
            iASSET_INCOME_STREAM, "Income Streams (Pensions/Annuities)");

    public static final ReferenceCode RC_LIABILITY = new ReferenceCode(
            iLIABILITY, "Liabilities");

    public static final ReferenceCode RC_CASHFLOW = new ReferenceCode(-2,
            "Cashflow"); // group

    public static final ReferenceCode RC_REGULAR_INCOME = new ReferenceCode(
            iREGULAR_INCOME, "Income");

    public static final ReferenceCode RC_REGULAR_EXPENSE = new ReferenceCode(
            iREGULAR_EXPENSE, "Out goings (Expenses)");

    public static final ReferenceCode RC_TAX_OFFSET = new ReferenceCode(
            iTAX_OFFSET, "Tax Offsets & Credits");

}