/*
 * GearingData.java
 *
 * Created on 11 September 2002, 09:34
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.BooleanCode;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.util.DateTimeUtils;

public class GearingData extends ClientPersonData {

    public Gearing gearing = new Gearing();

    /** Creates new GearingData */
    public GearingData() {
    }

    public class Gearing implements java.io.Serializable {

        public String Name = STRING_EMPTY;

        public String Years2Project = STRING_EMPTY;

        public String InvestmentStrategy = STRING_EMPTY;

        public String GrowthRate = STRING_EMPTY;

        public String IncomeRate = STRING_EMPTY;

        public String TotalReturnRate = STRING_EMPTY;

        public String InitialInvestorDepositAmount = STRING_EMPTY;

        public String OtherTaxableIncome = STRING_EMPTY;

        public String InitialLoanAmount = STRING_EMPTY;

        public String ReinvestIncome = STRING_EMPTY;

        public String CreditLimitAmount = STRING_EMPTY;

        public String Type = STRING_EMPTY;

        public String RegularInvestorContributionAmount = STRING_EMPTY;

        public String RegularLoanAdvanceAmount = STRING_EMPTY;

        // for graph
        public String graphIsReady = STRING_EMPTY;

        public Summary summary = new Summary();

        public class Summary implements java.io.Serializable {

            public String Graph = STRING_EMPTY;

            public String GraphName = STRING_EMPTY;

            public java.util.ArrayList Items = new java.util.ArrayList();

            public Item createItem() {
                return new Item();
            }

            public class Item implements java.io.Serializable {
                public String Year = STRING_EMPTY;

                public String OutlayAmount = STRING_EMPTY;

                public String InvestmentValueAmount = STRING_EMPTY;

                public String TotalLoanAmount = STRING_EMPTY;

                public String CapitalGrowthAmount = STRING_EMPTY;

                public String CGTAmount = STRING_EMPTY;

                public String PotentialOutcomeAmount = STRING_EMPTY;

                public String PotentialOutcomeNonGearingAmount = STRING_EMPTY;
            }

        }

    }

    public void init(PersonService person, Model model) throws java.io.IOException {

        if (model == null)
            model = Model.NONE;

        GearingCalc2 calc = new GearingCalc2();
        calc.setModel(model);
        init(person, calc);

    }

    public void init(PersonService person, GearingCalc2 calc)
            throws java.io.IOException {

        super.init(person);

        // gearing data
        gearing.Name = (calc.getModel().getTitle() == null) ? STRING_EMPTY
                : calc.getModel().getTitle();
        gearing.Years2Project = (number.toString(calc.getYears()) == null) ? STRING_EMPTY
                : number.toString(calc.getYears());
        gearing.Type = (calc.getGearingTypeDesc() == null) ? STRING_EMPTY
                : calc.getGearingTypeDesc();

        gearing.InvestmentStrategy = (calc.getInvestmentStrategyCodeDesc() == null) ? STRING_EMPTY
                : calc.getInvestmentStrategyCodeDesc();
        gearing.GrowthRate = (percent.toString(calc.getGrowthRate()) == null) ? STRING_EMPTY
                : percent.toString(calc.getGrowthRate());
        gearing.IncomeRate = (percent.toString(calc.getIncomeRate()) == null) ? STRING_EMPTY
                : percent.toString(calc.getIncomeRate());
        gearing.TotalReturnRate = (percent.toString(calc.getTotalReturnRate()) == null) ? STRING_EMPTY
                : percent.toString(calc.getTotalReturnRate());

        gearing.InitialInvestorDepositAmount = (currency.toString(calc
                .getInitialInvestorAmount()) == null) ? STRING_EMPTY : currency
                .toString(calc.getInitialInvestorAmount());
        gearing.InitialLoanAmount = (currency.toString(calc
                .getInitialLoanAmount()) == null) ? STRING_EMPTY : currency
                .toString(calc.getInitialLoanAmount());

        gearing.RegularInvestorContributionAmount = (currency.toString(calc
                .getRegularInvestorContributionAmount()) == null) ? STRING_EMPTY
                : currency
                        .toString(calc.getRegularInvestorContributionAmount());
        gearing.RegularLoanAdvanceAmount = (currency.toString(calc
                .getRegularLoanAdvanceAmount()) == null) ? STRING_EMPTY
                : currency.toString(calc.getRegularLoanAdvanceAmount());

        gearing.OtherTaxableIncome = (currency.toString(calc
                .getOtherTaxableIncomeAmount()) == null) ? STRING_EMPTY
                : currency.toString(calc.getOtherTaxableIncomeAmount());

        Boolean b = calc.getReinvestIncome();
        gearing.ReinvestIncome = b == null || !b.booleanValue() ? BooleanCode.rcNO
                .getCodeDesc()
                : BooleanCode.rcYES.getCodeDesc();

        gearing.CreditLimitAmount = (currency.toString(calc.getCreditLimit()) == null) ? STRING_EMPTY
                : currency.toString(calc.getCreditLimit());

        // dates
        java.util.Date[] dates = calc.getDates(FrequencyCode.YEARLY);
        // Outlay
        double[] outlay = calc.getOutlay();
        // Your Investment
        double[] investmentGeared = calc.getTotalInvested(true);
        // Total Loan
        double[] totalLoan = calc.getTotalLoan();
        // Capital Growth
        double[] capitalGrowth = calc.getCapitalGrowth();
        // CGT
        double[] cgt = calc.getCGT();
        // Potential Outcome
        double[] potentialOutcomeGeared = calc.getPotentialOutcome(true);
        // Potential Outcome (non-geared)
        double[] potentialOutcomeNonGeared = calc.getPotentialOutcome(false);

        if (dates != null) {

            GearingData.Gearing.Summary.Item item;

            for (int i = 0; i < dates.length; i++) { // ??? add in reverse
                                                        // order ???
                // for (int i = dates.length - 1; i >= 0 ; i-- ) {
                item = gearing.summary.createItem();

                item.Year = DateTimeUtils.asString(dates[i], "yyyy");
                item.OutlayAmount = (currency.toString(outlay[i]) == null) ? STRING_EMPTY
                        : currency.toString(outlay[i]);
                item.InvestmentValueAmount = (currency
                        .toString(investmentGeared[i]) == null) ? STRING_EMPTY
                        : currency.toString(investmentGeared[i]);
                item.TotalLoanAmount = (currency.toString(totalLoan[i]) == null) ? STRING_EMPTY
                        : currency.toString(totalLoan[i]);
                item.CapitalGrowthAmount = (currency.toString(capitalGrowth[i]) == null) ? STRING_EMPTY
                        : currency.toString(capitalGrowth[i]);
                item.CGTAmount = (currency.toString(cgt[i]) == null) ? STRING_EMPTY
                        : currency.toString(cgt[i]);
                item.PotentialOutcomeAmount = (currency
                        .toString(potentialOutcomeGeared[i]) == null) ? STRING_EMPTY
                        : currency.toString(potentialOutcomeGeared[i]);
                item.PotentialOutcomeNonGearingAmount = (currency
                        .toString(potentialOutcomeNonGeared[i]) == null) ? STRING_EMPTY
                        : currency.toString(potentialOutcomeNonGeared[i]);

                gearing.summary.Items.add(item);

            }

        }

    }

}
