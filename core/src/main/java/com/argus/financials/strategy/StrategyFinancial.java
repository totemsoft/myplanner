/*
 * FinancialUsed.java
 *
 * Created on 9 April 2002, 19:32
 */

package com.argus.financials.strategy;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.argus.financials.bean.AbstractBase;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.Liability;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.service.PersonService;

public class StrategyFinancial extends AbstractBase {

    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.strategy.StrategyFinancial

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 2285930253526011013L;

    public static final String ASSET_FINANCIAL = "New Asset Under Advice ...";

    public static final String ONEOFF_FINANCIAL = "New One-Off Income ...";

    public static final String CASHFLOW_FINANCIAL = "New Surplus Cashflow ...";

    private static int id;

    private Financial financial;

    private java.math.BigDecimal amount;

    /** Creates new StrategyFinancial */
    public StrategyFinancial(Financial f) {
        if (f == null)
            throw new IllegalArgumentException(
                    "-----> StrategyFinancial( financial == null ) <-----");
        financial = f;

        if (financial.isGenerated() || financial.getStrategyGroupID() != null) {
            // generated income, we have to use whole amount
            setAmount(financial.getAmount());
            financial.updateBalanceAmount(getAmount());
        }

        if (financial.getPrimaryKeyID() == null)
            financial.setPrimaryKeyID(new Integer(--id));
    }

    public String toString() {
        return financial == null ? super.toString() : financial.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof StrategyFinancial) {
            if (financial != null
                    && financial.equals(((StrategyFinancial) obj).financial))
                return true;
        }
        return super.equals(obj);
    }

    public void clear() {
        super.clear();

        amount = null;
        // setAmount(null);
    }

    /**
     * 
     */
    public Financial getFinancial() {
        return financial;
    }

    public Integer getObjectTypeID() {
        return financial.getObjectTypeID();
    }

    public Integer getFinancialPrimaryKeyID() {
        return getFinancial().getPrimaryKeyID();
    }

    public String getTypeDesc() {
        return getFinancial().getTypeDesc();
    }

    public java.math.BigDecimal getBalanceAmount(boolean signed) {
        return getFinancial().getBalanceAmount(signed);
    }

    public void setAmount(StrategyFinancial sf) {

        if (sf == null)
            return;

        if (getFinancial().getStrategyGroupID() == null)
            amount = amount == null ? sf.getAmount() : amount.add(sf
                    .getAmount());
        else
            amount = sf.getAmount();

        if (this.getFinancial() != sf.getFinancial())
            sf.setAmount((java.math.BigDecimal) null);

        setAmount(amount);

    }

    public void setAmount(java.math.BigDecimal value) {
        if (value != null && value.scale() > MONEY_SCALE)
            value = value
                    .setScale(MONEY_SCALE, java.math.BigDecimal.ROUND_DOWN);

        // if (DEBUG) System.out.println( "getAmount(true): " + getAmount(true)
        // );
        // if (DEBUG) System.out.println( "\tsetAmount( " + value + " )" );

        if (equals(amount, value))
            return;

        // add old value (return $$$ back to the pool)
        if (amount != null)
            getFinancial().updateBalanceAmount(getAmount());

        // if ( value != null && value.compareTo( getPoolBalanceAmount() ) > 0 )
        // value = getPoolBalanceAmount();

        amount = value;
        // if (DEBUG) System.out.println( "\tamount: " + amount );

        // subtract new value (borrow $$$ from the pool)
        if (amount != null)
            getFinancial().updateBalanceAmount(getAmount().negate());

        // if (DEBUG) System.out.println( "\t\tgetAmount(true): " +
        // getAmount(true) );

    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public java.math.BigDecimal getAmount(boolean signed) {
        if (amount == null)
            return null;
        if (!signed)
            return amount;
        return (getFinancial() instanceof RegularExpense || getFinancial() instanceof Liability) ? amount
                .negate()
                : amount;
    }

    public static Vector getAllFinancials(PersonService person) {

        Vector result = null;

        try {
            Map map = person.getFinancials();
            Iterator iter = map.values().iterator();

            while (iter.hasNext()) {
                Map map2 = (Map) iter.next();

                java.util.Iterator iter2 = map2.values().iterator();
                while (iter2.hasNext()) {
                    if (result == null)
                        result = new Vector();
                    result.add((Financial) iter2.next());
                }

            }

        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }

        return result;

    }

}
