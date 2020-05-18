/*
 * FinancialPool.java
 *
 * Created on 3 April 2002, 13:43
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * 
 * extension to Financial used to disperse/finance assets and regular
 * income/expense-liability
 * 
 * @version
 */

public class FinancialPoolItem extends com.argus.financials.bean.AbstractBase {

    private Financial financial;

    private java.math.BigDecimal poolAmount;

    private java.math.BigDecimal poolBalanceAmount;

    /** Creates new FinancialPool */
    FinancialPoolItem(Financial financial) {
        this.financial = financial;
        setId(financial.getId());
    }

    public void clear() {
        super.clear();

        poolAmount = null;
        poolBalanceAmount = null;
    }

    /**
     * get/set
     */
    public java.math.BigDecimal getPoolAmount() {
        return poolAmount;
    }

    public void setPoolAmount(java.math.BigDecimal value)
            throws NegativeAmountException {
        if (value != null && value.doubleValue() < 0)
            throw new NegativeAmountException(getClass().getName()
                    + ": setPoolAmount(" + value + ").");

        if (equals(poolAmount, value))
            return;

        if (value != null && value.doubleValue() == 0)
            value = null;

        poolAmount = value;
        poolBalanceAmount = value == null ? null : new java.math.BigDecimal(
                value.doubleValue());
        setModified(true);
    }

    public java.math.BigDecimal getPoolBalanceAmount() {
        return poolBalanceAmount;
    }

    public java.math.BigDecimal updatePoolBalanceAmount(
            java.math.BigDecimal value) // value can be negative
            throws NegativeAmountException {
        if (value == null || value.doubleValue() == 0)
            return poolBalanceAmount;

        if (value.doubleValue() < 0)
            if (poolBalanceAmount.add(value).doubleValue() < 0)
                throw new NegativeAmountException(getClass().getName()
                        + ": updatePoolBalanceAmount(" + value + ").");

        return poolBalanceAmount = poolBalanceAmount.add(value);

    }

}
