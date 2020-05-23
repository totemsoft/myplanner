/*
 * Strategy.java
 *
 * Created on 9 April 2002, 13:19
 */

package au.com.totemsoft.myplanner.strategy;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Iterator;
import java.util.Vector;

import au.com.totemsoft.myplanner.bean.AbstractBase;
import au.com.totemsoft.myplanner.bean.NegativeAmountException;
import au.com.totemsoft.myplanner.code.Code;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.projection.save.ModelCollection;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.util.ReferenceCode;

public class Strategy extends AbstractBase {

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
        return refCode.getCodeId();
    }

    public String getStrategyTitle() {
        return refCode.getDescription();
    }

    public String getCodeDescription() {
        return refCode.getDescription();
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

        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
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

        } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
        }

        return result;

    }

}
