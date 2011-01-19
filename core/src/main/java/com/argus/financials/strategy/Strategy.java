/*
 * Strategy.java
 *
 * Created on 9 April 2002, 13:19
 */

package com.argus.financials.strategy;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Iterator;
import java.util.Vector;

import com.argus.financials.bean.AbstractBase;
import com.argus.financials.bean.NegativeAmountException;
import com.argus.financials.code.Code;
import com.argus.financials.code.ModelType;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.service.PersonService;
import com.argus.util.ReferenceCode;

public class Strategy extends AbstractBase {

    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.strategy.Strategy

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 3310315389054817122L;

    public static final String NEW_STRATEGY = "New Strategy ...";

    private static Strategy[] strategies;

    private ReferenceCode refCode;

    private java.math.BigDecimal goalAmount;

    /** Creates new Strategy */
    public Strategy(ReferenceCode refCode) {
        this.refCode = refCode;
    }

    public String toString() {
        return getCodeDescription();
    }

    public String toHTMLString() {
        return "<html>" + "<b>" + toString() + "</b>" + "</html>";
    }

    // move to super-strategy
    public static Strategy[] getStrategies() {
        if (strategies == null) {
            java.util.Collection codes = new ModelType().getCodes();

            strategies = new Strategy[codes.size()];
            java.util.Iterator iter = codes.iterator();
            int i = 0;
            while (iter.hasNext()) {
                ReferenceCode refCode = (ReferenceCode) iter.next();
                strategies[i++] = new Strategy(refCode);
            }

        }
        return strategies;
    }

    /**
     * 
     */
    public ReferenceCode getReferenceCode() {
        return refCode;
    }

    public Integer getStrategyTypeID() {
        return refCode.getCodeIDInteger();
    }

    public String getStrategyTitle() {
        return refCode.getCodeDesc();
    }

    public String getCodeDescription() {
        return refCode.getCodeDesc();
    }

    public java.math.BigDecimal getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(java.math.BigDecimal value)
            throws NegativeAmountException {
        if (equals(goalAmount, value))
            return;

        goalAmount = value;
        this.setModified(true);
    }

    /**
     * for this strategyTypeID
     */
    public Object[] getModels(PersonService person) {

        Object[] result = Code.EMPTY_KEYS;

        try {
            Vector models = person.getModels(getStrategyTypeID());
            if (models != null && models.size() > 0) {
                result = new StrategyModel[models.size()];

                int i = 0;
                java.util.Iterator iter = models.iterator();
                while (iter.hasNext())
                    result[i++] = new StrategyModel((Model) iter.next());
            }

        } catch (com.argus.financials.service.client.ServiceException e) {
            e.printStackTrace(System.err);
        }

        return result;

    }

    public static Vector getAllModels(PersonService person) {

        Vector result = null;

        try {
            ModelCollection mc = person.getModels();
            Iterator iter = mc.valuesIterator();

            while (iter.hasNext()) {
                Vector models = (Vector) iter.next();

                java.util.Iterator iter2 = models.iterator();
                while (iter2.hasNext()) {
                    if (result == null)
                        result = new Vector();
                    result.add(iter2.next());
                }

            }

        } catch (com.argus.financials.service.client.ServiceException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
        }

        return result;

    }

}
