/*
 * TaxData.java
 *
 * Created on 26 September 2002, 15:47
 */

package com.argus.financials.report.data;

/**
 * 
 * @author Serguei Piltiaev
 * @version
 */

import com.argus.beans.AbstractComponentModel;
import com.argus.financials.projection.GeneralTaxCalculator;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.financials.tax.au.ITaxConstants;

public class TaxData extends ClientPersonData {

    public Tax tax = new Tax();

    /** Creates new GearingData */
    public TaxData() {
    }

    public class Tax implements java.io.Serializable {

        public String iOther = STRING_EMPTY;

        public String iSalary = STRING_EMPTY;

        public String iLumpSumPartA = STRING_EMPTY;

        public String iLumpSumPartB = STRING_EMPTY;

        public String iLumpSumMTR = STRING_EMPTY;

        public String iEtpNonExcessive = STRING_EMPTY;

        public String iEtpExcessive = STRING_EMPTY;

        public String iPensionsAllowance = STRING_EMPTY;

        public String iOtherPensions = STRING_EMPTY;

        public String iRFB = STRING_EMPTY;

        public String iGrossInterest = STRING_EMPTY;

        public String iDividendsUnfranked = STRING_EMPTY;

        public String iDividendsFranked = STRING_EMPTY;

        public String iDividendsImputationCredit = STRING_EMPTY;

        public String iTaxDeferred = STRING_EMPTY;

        public String iSuppliment = STRING_EMPTY;

        public String iTotalIncome = STRING_EMPTY;

        public String tOther = STRING_EMPTY;

        public String tSalary = STRING_EMPTY;

        public String tLumpSumPartA = STRING_EMPTY;

        public String tLumpSumPartB = STRING_EMPTY;

        public String tPensionsAllowance = STRING_EMPTY;

        public String tOtherPensions = STRING_EMPTY;

        public String tETP = STRING_EMPTY;

        public String tGrossInterest = STRING_EMPTY;

        public String tDividends = STRING_EMPTY;

        public String dGeneral = STRING_EMPTY;

        public String dInterestDividends = STRING_EMPTY;

        public String dUPP = STRING_EMPTY;

        public String pSpouseIncome = STRING_EMPTY;

        public String pDependants = STRING_EMPTY;

        public String pAge = STRING_EMPTY;

        public String oTaxWithheld = STRING_EMPTY;

        public String oImputationCredit = STRING_EMPTY;

        public String oLumpSumETP = STRING_EMPTY;

        public String oLowIncome = STRING_EMPTY;

        public String cTaxableIncome = STRING_EMPTY;

        public String cGrossTax = STRING_EMPTY;

        public String cML = STRING_EMPTY;

        public String cMLS = STRING_EMPTY;

        public String cTaxPayable = STRING_EMPTY;

        public String cNetIncome = STRING_EMPTY;

    }

    public void init(PersonService person, Model model) throws java.io.IOException {

        if (model == null)
            model = Model.NONE;

        GeneralTaxCalculator dm = new GeneralTaxCalculator();

        String values = model.getData();
        dm.setValuesFromObject(values == null ? "" : values);
        dm.sendNotification(new Object());
        dm.setModified(false);

        init(person, dm);

    }

    public void init(PersonService person, AbstractComponentModel dm)
            throws java.io.IOException {

        super.init(person);

        // gearing data
        tax.iOther = currency.toString(dm.getDouble(ITaxConstants.I_OTHER));
        tax.iSalary = currency.toString(dm.getDouble(ITaxConstants.I_SALARY));
        tax.iLumpSumPartA = currency.toString(dm
                .getDouble(ITaxConstants.I_LUMP_SUM_PART_A));
        tax.iLumpSumPartB = currency.toString(dm
                .getDouble(ITaxConstants.I_LUMP_SUM_PART_B));
        tax.iLumpSumMTR = currency.toString(dm
                .getDouble(ITaxConstants.I_LUMP_SUM_PART_C));
        tax.iEtpNonExcessive = currency.toString(dm
                .getDouble(ITaxConstants.I_ETP_NON_EXCESSIVE));
        tax.iEtpExcessive = currency.toString(dm
                .getDouble(ITaxConstants.I_ETP_EXCESSIVE));
        tax.iPensionsAllowance = currency.toString(dm
                .getDouble(ITaxConstants.I_PENSIONS_ALLOWANCE));
        tax.iOtherPensions = currency.toString(dm
                .getDouble(ITaxConstants.I_OTHER_PENSIONS));
        tax.iRFB = currency.toString(dm.getDouble(ITaxConstants.I_RFB));
        tax.iGrossInterest = currency.toString(dm
                .getDouble(ITaxConstants.I_GROSS_INTEREST));
        tax.iDividendsUnfranked = currency.toString(dm
                .getDouble(ITaxConstants.I_DIVIDENDS_UNFRANKED));
        tax.iDividendsFranked = currency.toString(dm
                .getDouble(ITaxConstants.I_DIVIDENDS_FRANKED));
        tax.iDividendsImputationCredit = currency.toString(dm
                .getDouble(ITaxConstants.I_DIVIDENDS_IMPUTATION_CREDIT));
        tax.iTaxDeferred = currency.toString(dm
                .getDouble(ITaxConstants.I_TAX_DEFERRED));
        tax.iSuppliment = currency.toString(dm
                .getDouble(ITaxConstants.I_SUPPLEMENT));
        tax.iTotalIncome = currency.toString(dm
                .getDouble(ITaxConstants.I_TOTAL_INCOME));
        tax.tOther = currency.toString(dm.getDouble(ITaxConstants.T_OTHER));
        tax.tSalary = currency.toString(dm.getDouble(ITaxConstants.T_SALARY));
        tax.tLumpSumPartA = currency.toString(dm
                .getDouble(ITaxConstants.T_LUMP_SUM_PART_A));
        tax.tLumpSumPartB = currency.toString(dm
                .getDouble(ITaxConstants.T_LUMP_SUM_PART_B));
        tax.tPensionsAllowance = currency.toString(dm
                .getDouble(ITaxConstants.T_PENSIONS_ALLOWANCE));
        tax.tOtherPensions = currency.toString(dm
                .getDouble(ITaxConstants.T_OTHER_PENSIONS));
        tax.tETP = currency.toString(dm.getDouble(ITaxConstants.T_ETP));
        tax.tGrossInterest = currency.toString(dm
                .getDouble(ITaxConstants.T_GROSS_INTEREST));
        tax.tDividends = currency.toString(dm
                .getDouble(ITaxConstants.T_DIVIDENDS));

        tax.dGeneral = currency.toString(dm.getDouble(ITaxConstants.D_GENERAL));
        tax.dInterestDividends = currency.toString(dm
                .getDouble(ITaxConstants.D_INTEREST_DIVIDEND));
        tax.dUPP = currency.toString(dm
                .getDouble(ITaxConstants.D_UNDEDUCTED_PIRCHASE_PRICE));

        tax.pSpouseIncome = currency.toString(dm
                .getDouble(ITaxConstants.P_SPOUSE_INCOME));
        tax.pDependants = currency.toString(dm
                .getDouble(ITaxConstants.P_DEPENDANTS));
        tax.pAge = currency.toString(dm.getDouble(ITaxConstants.P_AGE));

        String pHospital = (ITaxConstants.P_HOSPITAL_COVER == null) ? STRING_EMPTY
                : dm.getValue(ITaxConstants.P_HOSPITAL_COVER);
        String pMaritalStatus = (ITaxConstants.P_MARITAL_STATUS == null) ? STRING_EMPTY
                : dm.getValue(ITaxConstants.P_MARITAL_STATUS);

        tax.oTaxWithheld = currency.toString(dm
                .getDouble(ITaxConstants.O_TAX_WITHHELD));
        tax.oImputationCredit = currency.toString(dm
                .getDouble(ITaxConstants.O_IMPUTATION_CREDIT));
        tax.oLumpSumETP = currency.toString(dm
                .getDouble(ITaxConstants.O_LUMP_SUM_ETP));
        tax.oLowIncome = currency.toString(dm
                .getDouble(ITaxConstants.O_LOW_INCOME));

        tax.cTaxableIncome = currency.toString(dm
                .getDouble(ITaxConstants.TAXABLE_INCOME));
        tax.cGrossTax = currency.toString(dm
                .getDouble(ITaxConstants.TAX_ON_TAXABLE_INCOME));
        tax.cML = currency.toString(dm.getDouble(ITaxConstants.ML));
        tax.cMLS = currency.toString(dm.getDouble(ITaxConstants.MLS));

        tax.cTaxPayable = currency.toString(dm
                .getDouble(ITaxConstants.TAX_PAYABLE));

        tax.cNetIncome = String.valueOf(dm
                .getDouble(ITaxConstants.I_TOTAL_INCOME)
                - dm.getDouble(ITaxConstants.TAX_PAYABLE));

    }

}
