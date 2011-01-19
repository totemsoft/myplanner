/*
 * FinancialGoal.java
 *
 * Created on 16 October 2002, 10:50
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 */

public class FinancialGoalData extends BaseData {

    public String TargetAge = "";

    public String TargetDate = "";

    public String YearsToTargetAge = "";

    public String TargetIncome = "";

    public String LumpSumRequired = "";

    public String Notes = "";

    /** Creates a new instance of FinancialGoalData */
    public FinancialGoalData() {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void clear() {
        TargetAge = "";
        TargetDate = "";
        YearsToTargetAge = "";
        TargetIncome = "";
        LumpSumRequired = "";
        Notes = "";

    }

    public void init(com.argus.financials.service.PersonService person)
            throws com.argus.financials.service.client.ServiceException {

        if (person == null)
            return;

        com.argus.financials.bean.FinancialGoal financialGoal = person
                .getFinancialGoal();
        if (financialGoal == null) {
            clear();
            return;
        }

        Integer targetAge = financialGoal.getTargetAge();
        TargetAge = targetAge == null ? "" : targetAge.toString();

        java.util.Date targetDate = financialGoal.getTargetDate(person
                .getDateOfBirth());
        TargetDate = targetDate == null ? "" : com.argus.util.DateTimeUtils
                .formatAsSHORT(targetDate);

        Integer yearsToTargetAge = financialGoal.getYearsToTargetAge(person
                .getDateOfBirth());
        YearsToTargetAge = yearsToTargetAge == null ? "" : yearsToTargetAge
                .toString();

        TargetIncome = financialGoal.getTargetIncome() == null ? "0" : currency
                .toString(financialGoal.getTargetIncome());
        LumpSumRequired = financialGoal.getLumpSumRequired() == null ? "0"
                : currency.toString(financialGoal.getLumpSumRequired());

        Notes = financialGoal.getNotes();
    }

}
