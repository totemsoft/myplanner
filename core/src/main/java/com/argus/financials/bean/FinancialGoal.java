/*
 * FinancialGoal.java
 *
 * Created on 15 November 2001, 14:58
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.etc.FPSAssignableObject;

public class FinancialGoal extends FPSAssignableObject {

    private Integer goalCodeID; // e.g. WealthAccumulation, Retirement

    private Integer targetAge;

    private java.math.BigDecimal targetIncome;

    private Integer yearsIncomeReq;

    private java.math.BigDecimal residualReq;

    private java.math.BigDecimal lumpSumRequired;

    private java.math.BigDecimal inflation;

    private Integer targetStrategyID;

    private String notes;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.FINANCIAL_GOAL);

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new FinancialGoal */
    public FinancialGoal() {
    }

    public FinancialGoal(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methods
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof FinancialGoal))
            return;

        FinancialGoal fg = (FinancialGoal) value;

        goalCodeID = fg.goalCodeID;
        targetAge = fg.targetAge;
        targetIncome = fg.targetIncome;
        yearsIncomeReq = fg.yearsIncomeReq;
        residualReq = fg.residualReq;
        lumpSumRequired = fg.lumpSumRequired;
        inflation = fg.inflation;
        targetStrategyID = fg.targetStrategyID;
        notes = fg.notes;
        setModified(true);
    }

    /**
     * helper methods
     */
    protected void clear() {
        super.clear();

        goalCodeID = null;
        targetAge = null;
        targetIncome = null;
        yearsIncomeReq = null;
        residualReq = null;
        lumpSumRequired = null;
        inflation = null;
        targetStrategyID = null;
        notes = null;
    }

    /**
     * get/set methods
     */
    public Integer getGoalCodeID() {
        return goalCodeID;
    }

    public void setGoalCodeID(Integer value) throws InvalidCodeException {
        if (equals(goalCodeID, value))
            return;

        // new GoalCode().isValidThrow( value );

        goalCodeID = value;
        setModified(true);
    }

    public Integer getTargetAge() {
        return targetAge;
    }

    public void setTargetAge(Integer value) {
        if (equals(targetAge, value))
            return;

        targetAge = value;
        setModified(true);
    }

    public java.util.Date getTargetDate(java.util.Date dateOfBirth) {
        if (targetAge == null || dateOfBirth == null)
            return null;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(dateOfBirth);
        calendar.add(java.util.Calendar.YEAR, targetAge.intValue());
        return calendar.getTime();
    }

    public Integer getYearsToTargetAge(java.util.Date dateOfBirth) {
        java.util.Date targetDate = getTargetDate(dateOfBirth);
        if (targetDate == null)
            return null;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int years = calendar.get(java.util.Calendar.YEAR);

        calendar.setTime(targetDate);
        return new Integer(calendar.get(java.util.Calendar.YEAR) - years);
    }

    public java.math.BigDecimal getTargetIncome() {
        return targetIncome;
    }

    public void setTargetIncome(java.math.BigDecimal value) {
        if (equals(targetIncome, value))
            return;

        targetIncome = value;
        setModified(true);
    }

    public Integer getYearsIncomeReq() {
        return yearsIncomeReq;
    }

    public void setYearsIncomeReq(Integer value) {
        if (equals(yearsIncomeReq, value))
            return;

        yearsIncomeReq = value;
        setModified(true);
    }

    public java.math.BigDecimal getResidualReq() {
        return residualReq;
    }

    public void setResidualReq(java.math.BigDecimal value) {
        if (equals(residualReq, value))
            return;

        residualReq = value;
        setModified(true);
    }

    public java.math.BigDecimal getLumpSumRequired() {
        return lumpSumRequired;
    }

    public void setLumpSumRequired(java.math.BigDecimal value) {
        if (equals(lumpSumRequired, value))
            return;

        lumpSumRequired = value;
        setModified(true);
    }

    public java.math.BigDecimal getInflation() {
        return inflation;
    }

    public void setInflation(java.math.BigDecimal value) {
        if (equals(inflation, value))
            return;
        inflation = value;
        setModified(true);
    }

    public Integer getTargetStrategyID() {
        return targetStrategyID;
    }

    public void setTargetStrategyID(Integer value) {
        if (equals(targetStrategyID, value))
            return;

        targetStrategyID = value;
        setModified(true);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String value) {
        if (equals(notes, value))
            return;

        notes = value;
        setModified(true);
    }

}
