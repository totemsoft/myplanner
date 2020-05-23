/*
 * Survey.java
 *
 * Created on 16 October 2001, 14:30
 */

package au.com.totemsoft.myplanner.etc;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import au.com.totemsoft.myplanner.code.InvestmentStrategyCode;
import au.com.totemsoft.qaa.QuestionAndAnswers;

public class Survey extends QuestionAndAnswers
//extends FPSAssignableObject 
{
    private Integer primaryKeyID;

    private Integer ownerPrimaryKeyID;

    private Integer selectedInvestmentStrategy = null;

    private boolean notify = true;
    transient private java.util.Collection changeListeners;
    // transient private javax.swing.event.EventListenerList changeListeners;

    /** Creates new Survey */
    public Survey() {
        super();
    }

    public Survey(Integer ownerPrimaryKeyID) {
        this.ownerPrimaryKeyID = ownerPrimaryKeyID;
    }

    public Integer getId() {
        return primaryKeyID;
    }

    public void setId(Integer value) {
        primaryKeyID = value;
    }

    public Integer getOwnerPrimaryKeyID() {
        return ownerPrimaryKeyID;
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        ownerPrimaryKeyID = value;
    }
    
    public boolean isModified() {
        return super.isModified() || primaryKeyID == null;
    }

    public void setModified(boolean value) {
        super.setModified(value);
        if (value)
            notifyChangeListeners(); // on AbstractBase
    }

    public boolean isNotify() {
        return notify;
    }

    public void disableNotify() {
        notify = false;
    }

    public void enableNotify() {
        notify = true;
        notifyChangeListeners();
    }

    public void addChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new java.util.ArrayList();
        // changeListeners = new javax.swing.event.EventListenerList();
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
            // to update this listener state
            // listener.stateChanged( new javax.swing.event.ChangeEvent(this) );
        }
    }

    public void removeChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null)
            return;
        if (listener == null) // remove all
            changeListeners.clear();
        else if (changeListeners.contains(listener))
            changeListeners.remove(listener);
    }

    protected void notifyChangeListeners() {
        if (changeListeners == null || !notify)
            return;
        final ChangeEvent changeEvent = new ChangeEvent(this);
        Iterator iter = changeListeners.iterator();
        while (iter.hasNext()) {
            final ChangeListener listener = (ChangeListener) iter.next();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.stateChanged(changeEvent);
                }
            });
        }
    }

    // can not be set manualy (override getSelectedInvestmentStrategy() )
    public Integer getRiskProfileID() {
        if (!isReady())
            return null;

        int score = getScore();

        /*
         * if ( score <= 18 ) return InvestmentStrategyCode.CONSERVATIVE; if (
         * score <= 36 ) return InvestmentStrategyCode.CONSERVATIVE_BALANCED; if (
         * score <= 54 ) return InvestmentStrategyCode.BALANCED_GROWTH; if (
         * score <= 72 ) return InvestmentStrategyCode.GROWTH; //if ( score <=
         * 90 ) return InvestmentStrategyCode.AGGRESSIVE_GROWTH;
         */

        // added two new profiles (Ultra Conservative and Ultra Growth) and updated if conditions
        Integer risk_profile = null;
        if (score <= 28) // ( score >= 20 && score <= 28 )
            risk_profile = InvestmentStrategyCode.ULTRA_CONSERVATIVE;
        else if (score >= 29 && score <= 37)
            risk_profile = InvestmentStrategyCode.CONSERVATIVE;
        else if (score >= 38 && score <= 46)
            risk_profile = InvestmentStrategyCode.CONSERVATIVE_BALANCED;
        else if (score >= 47 && score <= 55)
            risk_profile = InvestmentStrategyCode.BALANCED_GROWTH;
        else if (score >= 56 && score <= 64)
            risk_profile = InvestmentStrategyCode.GROWTH;
        else if (score >= 65 && score <= 74)
            risk_profile = InvestmentStrategyCode.AGGRESSIVE_GROWTH;
        else if (score >= 75) // ( score >= 74 && score <= 81 )
            risk_profile = InvestmentStrategyCode.ULTRA_GROWTH;
        return risk_profile;
    }

    public String getRiskProfileStrategyTitle() {
        Integer strategy = getRiskProfileID();
        return strategy == null ? null : new InvestmentStrategyCode()
                .getCodeDescription(strategy);
    }

    // can be set manualy (do not override getRiskProfileID() )
    public Integer getSelectedInvestmentStrategy() {
        Integer riskProfileID = getRiskProfileID();
        return riskProfileID == null ? selectedInvestmentStrategy
                : riskProfileID;
        // return selectedInvestmentStrategy;
    }

    public void setSelectedInvestmentStrategy(Integer value) {
        if (equals(selectedInvestmentStrategy, value))
            return;

        selectedInvestmentStrategy = value;
        setModified(true);
    }

}
