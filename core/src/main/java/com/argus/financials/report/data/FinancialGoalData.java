/*
 * FinancialGoal.java
 *
 * Created on 16 October 2002, 10:50
 */

package com.argus.financials.report.data;

import com.argus.financials.api.bean.IPerson;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.service.PersonService;

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

    public void init(PersonService person)
            throws com.argus.financials.api.ServiceException {

        if (person == null)
            return;

        IPerson personName = person.getPersonName();

        FinancialGoal financialGoal = person.getFinancialGoal();
        if (financialGoal == null) {
            clear();
            return;
        }

        Integer targetAge = financialGoal.getTargetAge();
        TargetAge = targetAge == null ? "" : targetAge.toString();

        java.util.Date targetDate = financialGoal.getTargetDate(personName.getDateOfBirth());
        TargetDate = targetDate == null ? "" : com.argus.util.DateTimeUtils
                .formatAsSHORT(targetDate);

        Integer yearsToTargetAge = financialGoal.getYearsToTargetAge(personName.getDateOfBirth());
        YearsToTargetAge = yearsToTargetAge == null ? "" : yearsToTargetAge.toString();

        TargetIncome = financialGoal.getTargetIncome() == null ? "0" : currency
                .toString(financialGoal.getTargetIncome());
        LumpSumRequired = financialGoal.getLumpSumRequired() == null ? "0"
                : currency.toString(financialGoal.getLumpSumRequired());

        Notes = financialGoal.getNotes();
    }

}
