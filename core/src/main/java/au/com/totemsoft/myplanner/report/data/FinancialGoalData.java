/*
 * FinancialGoal.java
 *
 * Created on 16 October 2002, 10:50
 */

package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.api.bean.IPerson;
import au.com.totemsoft.myplanner.bean.FinancialGoal;
import au.com.totemsoft.myplanner.service.PersonService;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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
            throws au.com.totemsoft.myplanner.api.ServiceException {

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
        TargetDate = targetDate == null ? "" : au.com.totemsoft.util.DateTimeUtils
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
