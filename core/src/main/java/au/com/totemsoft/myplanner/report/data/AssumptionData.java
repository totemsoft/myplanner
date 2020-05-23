/*
 * AssumptionData.java
 *
 * Created on 27 February 2003, 13:53
 */

package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.bean.Assumptions;
import au.com.totemsoft.myplanner.code.BooleanCode;
import au.com.totemsoft.util.DateTimeUtils;

public class AssumptionData extends BaseData {
    /*
     * public String clientName; public String clientDOB; public String
     * partnerName; public String partnerDOB;
     */
    public String Inflation;

    public String RetirementDate;

    public String YearsToProject;

    // public String displayMode;
    // public String displayModeRetirement;

    public String RetirementIncome;

    public String RetirementIncomeAtRetirement;

    public String LumpSumRequired;

    public String LumpSumRequiredAtRetirement;

    public String DebtRepayment; // Yes/No BooleanCode class

    public String InvestmentStrategy;

    /** Creates a new instance of AssumptionData */
    public AssumptionData() {
        clear();
    }

    protected void clear() {

        Inflation = STRING_EMPTY;
        RetirementDate = STRING_EMPTY;
        YearsToProject = STRING_EMPTY;

        RetirementIncome = STRING_EMPTY;
        RetirementIncomeAtRetirement = STRING_EMPTY;

        LumpSumRequired = STRING_EMPTY;
        LumpSumRequiredAtRetirement = STRING_EMPTY;

        DebtRepayment = STRING_EMPTY;
        InvestmentStrategy = STRING_EMPTY;

        super.clear();

    }

    public void init(au.com.totemsoft.myplanner.service.PersonService person)
            throws Exception {

        super.init(person);
        if (person == null)
            return;

        Assumptions a = new Assumptions();
        a.update(person);
        init(a);

    }

    public void init(Assumptions a) throws Exception {

        super.init(null);
        if (a == null)
            return;

        Inflation = percent.toString(a.getInflation());

        RetirementDate = DateTimeUtils.formatAsMEDIUM(a.getRetirementDate());

        YearsToProject = number.toString(a.getYearsToProject());

        RetirementIncome = money.toString(a.getRetirementIncome());
        RetirementIncomeAtRetirement = money.toString(a
                .getRetirementIncomeAtRetirement());

        LumpSumRequired = money.toString(a.getLumpSumRequired());
        LumpSumRequiredAtRetirement = money.toString(a
                .getLumpSumRequiredAtRetirement());

        DebtRepayment = a.getDebtRepayment() ? BooleanCode.rcYES.getDescription()
                : BooleanCode.rcNO.getDescription();
        InvestmentStrategy = a.getInvestmentStrategyDesc();

    }

}
