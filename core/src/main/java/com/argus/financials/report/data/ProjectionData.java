/*
 * ProjectionData.java
 *
 * Created on 14 March 2002, 08:00
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * used in QUICKView Report
 */

import com.argus.financials.code.FinancialClassID;
import com.argus.financials.code.SexCode;
import com.argus.financials.projection.AssetGrowthLinked;
import com.argus.financials.projection.CurrentPositionCalc;
import com.argus.financials.service.PersonService;
import com.argus.util.DateTimeUtils;

public class ProjectionData extends ClientPersonData implements
        FinancialClassID {

    public Data data; // initial data

    public BeforeRetirement beforeRetirement;

    public AfterRetirement afterRetirement; // can be null

    public Result result;

    public ProjectionData() {

        beforeRetirement = new BeforeRetirement();
        afterRetirement = new AfterRetirement();
        data = new Data();
        result = new Result();

    }

    public class Data implements java.io.Serializable {

        public String ReportName = STRING_EMPTY;

        public String Retirement = STRING_EMPTY; // Boolean.TRUE,
                                                    // Boolean.FALSE

        public String CashContribution = STRING_EMPTY; // CashContribution

        public String SuperContribution = STRING_EMPTY; // SuperContribution

        public String SavingsContribution = STRING_EMPTY; // SavingsContribution

        public String CashProjected = STRING_EMPTY; // CashProjected

        public String SuperProjected = STRING_EMPTY; // SuperProjected

        public String SavingsProjected = STRING_EMPTY; // SavingsProjected

        public String TotalProjected = STRING_EMPTY; // TotalProjected

        public String Indexation = STRING_EMPTY;

        public String Inflation = STRING_EMPTY;

        public String EntryFees = STRING_EMPTY;

        public String ReviewFees = STRING_EMPTY;

        public String ClientTaxRate = STRING_EMPTY;

        public String PartnerTaxRate = STRING_EMPTY;

        public String RequiredIncome = STRING_EMPTY; // Data.RequiredIncome

        public String PensionRebate = STRING_EMPTY; // Data.PensionRebate

        public String ATOUPP = STRING_EMPTY; // Data.ATOUPP

        public String DSSUPP = STRING_EMPTY; // Data.DSSUPP

        public String ResidualRequired = STRING_EMPTY; // Data.ResidualRequired

    }

    public class BeforeRetirement implements java.io.Serializable {

        public String Title = STRING_EMPTY; // BeforeRetirement.Title

        public String IncomeRate = STRING_EMPTY; // BeforeRetirement.IncomeRate

        public String GrowthRate = STRING_EMPTY; // BeforeRetirement.GrowthRate

        public String TotalReturnRate = STRING_EMPTY; // BeforeRetirement.TotalReturnRate

        public String Graph = STRING_EMPTY; // BeforeRetirement.Graph (file
                                            // name)

    }

    public class AfterRetirement implements java.io.Serializable {

        public String Title = STRING_EMPTY; // AfterRetirement.Title

        public String IncomeRate = STRING_EMPTY; // AfterRetirement.IncomeRate

        public String GrowthRate = STRING_EMPTY; // AfterRetirement.GrowthRate

        public String TotalReturnRate = STRING_EMPTY; // AfterRetirement.TotalReturnRate

        public String Graph = STRING_EMPTY; // AfterRetirement.Graph (file name)

    }

    public class Result implements java.io.Serializable {

        public String Comment = STRING_EMPTY; // Result.Comment

        public String Graph = STRING_EMPTY; // Result.Graph (file name)

        public String IncomePayable = STRING_EMPTY; // Result.IncomePayable

    }

    public void init(PersonService person, CurrentPositionCalc calc)
            throws Exception {

        super.init(person);

        if (person == null) {
            client.FullName = STRING_EMPTY;
            client.DOB = DateTimeUtils.formatAsMEDIUM(calc.getDateOfBirth());
            client.age = "" + calc.getAge();
            client.Sex = SexCode.MALE.equals(calc.getSexCodeID()) ? "Male"
                    : "Female";
            if (calc.getSingle()) {
                client.MaritalCode = "Single";
                getPartner().FullName = STRING_EMPTY;
                data.PartnerTaxRate = STRING_EMPTY;
            } else {
                client.MaritalCode = "Joint";
                data.PartnerTaxRate = percent
                        .toString(calc.getPartnerTaxRate());
            }

        } else {

            // partner person
            if (calc.getSingle()) {
                client.MaritalCode = "Single";
                getPartner().FullName = STRING_EMPTY;
                data.PartnerTaxRate = STRING_EMPTY;
            } else {
                data.PartnerTaxRate = percent
                        .toString(calc.getPartnerTaxRate());
            }

        }

        client.TargetAge = "" + calc.getTargetAge();
        client.TargetDate = DateTimeUtils.formatAsMEDIUM(calc.getTargetDate());
        client.YearsToProject = "" + calc.getYearsIntSpend();
        client.lifeExpectancy = "" + calc.getLifeExpectancyAfterRetire();

        // FeesAndTaxes
        data.Indexation = percent.toString(calc.getIndexRate()); // ???
        data.Inflation = percent.toString(calc.getIndexRate());
        data.EntryFees = percent.toString(calc.getEntryFeeRate());
        data.ReviewFees = percent.toString(calc.getRevisionFeeRate());
        data.ClientTaxRate = percent.toString(calc.getTaxRate());

        AssetGrowthLinked ag = calc.getAsset(ASSET_CASH);
        data.CashContribution = currency.toString(ag.getAddValue());
        double amount = ag.getTargetValue();
        data.CashProjected = currency.toString(amount);

        ag = calc.getAsset(ASSET_SUPERANNUATION);
        data.SuperContribution = currency.toString(ag.getAddValue());
        amount += ag.getTargetValue();
        data.SuperProjected = currency.toString(amount);

        ag = calc.getAsset(ASSET_INVESTMENT);
        data.SavingsContribution = currency.toString(ag.getAddValue());
        amount += ag.getTargetValue();
        data.SavingsProjected = currency.toString(amount);

        data.TotalProjected = currency.toString(amount);

        data.RequiredIncome = currency.toString(calc.getSpendValue());
        data.PensionRebate = percent.toString(calc.getRebateRate());
        data.ATOUPP = currency.toString(calc.getUndeductedPurchasePriceATO());
        data.DSSUPP = currency.toString(calc.getUndeductedPurchasePriceDSS());
        data.ResidualRequired = currency.toString(calc.getResidualValue());

    }

}
