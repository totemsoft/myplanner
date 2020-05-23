/*
 * IncomeStreamBean.java
 *
 * Created on 13 November 2002, 14:47
 */

package au.com.totemsoft.myplanner.bean.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.IncomeStream;

public class IncomeStreamBean extends AssetSuperannuationBean {

    /** Creates a new instance of IncomeStreamBean */
    public IncomeStreamBean() {
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return IncomeStream.class;
    }

    public Financial getNewFinancial() {
        return new IncomeStream();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.INCOME_STREAM;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_INCOMESTREAM;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    protected java.math.BigDecimal getRebateable() {
        return ((IncomeStream) getFinancial()).getRebateable();
    }

    protected void setRebateable(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setRebateable(value);
    }

    protected java.math.BigDecimal getPurchaseCost() {
        return ((IncomeStream) getFinancial()).getPurchaseCost();
    }

    protected void setPurchaseCost(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setPurchaseCost(value);
    }

    protected java.math.BigDecimal getIncome() {
        return ((IncomeStream) getFinancial()).getIncome();
    }

    protected void setIncome(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setIncome(value);
    }

    protected Integer getFrequencyCodeID() {
        return ((IncomeStream) getFinancial()).getFrequencyCodeID();
    }

    protected void setFrequencyCodeID(Integer value) {
        ((IncomeStream) getFinancial()).setFrequencyCodeID(value);
    }

    protected java.math.BigDecimal getDeductibleDSS() {
        return ((IncomeStream) getFinancial()).getDeductibleDSS();
    }

    protected void setDeductibleDSS(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setDeductibleDSS(value);
    }

    protected boolean isComplyingForDSS() {
        return ((IncomeStream) getFinancial()).isComplyingForDSS();
    }

    protected void setComplyingForDSS(boolean value) {
        ((IncomeStream) getFinancial()).setComplyingForDSS(value);
    }

    protected java.math.BigDecimal getIndexation() {
        return ((IncomeStream) getFinancial()).getIndexation();
    }

    protected void setIndexation(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setIndexation(value);
    }

    protected java.math.BigDecimal getUpfrontFee() {
        return ((IncomeStream) getFinancial()).getUpfrontFee();
    }

    protected void setUpfrontFee(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setUpfrontFee(value);
    }

    protected java.math.BigDecimal getOngoingFee() {
        return ((IncomeStream) getFinancial()).getOngoingFee();
    }

    protected void setOngoingFee(java.math.BigDecimal value) {
        ((IncomeStream) getFinancial()).setOngoingFee(value);
    }

    protected java.util.Date getEndDate() {
        return ((IncomeStream) getFinancial()).getEndDate();
    }

    protected void setEndDate(java.util.Date value) {
        ((IncomeStream) getFinancial()).setEndDate(value);
    }

}
