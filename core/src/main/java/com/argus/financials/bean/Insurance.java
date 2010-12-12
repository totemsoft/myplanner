/*
 * Insurance.java
 *
 * Created on 8 October 2001, 10:46
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.etc.FPSAssignableObject;

public class Insurance extends RegularExpense {

    /** Creates new Insurance */
    public Insurance(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methods
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Insurance))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Insurance i = (Insurance) value;

        // maturityDate = ac.maturityDate;

    }

    /**
     * helper methods
     */
    protected void clear() {
        super.clear();

        // maturityDate = null;

    }

    public static String[] getColumnNames() {
        return new String[] { "ID", "Description", "Cash Asset Type",
                "Maturity Date", "Owner", "Value" };
    }

    public Object[] getData() {
        return new Object[] { getFinancialDesc(), getFinancialTypeID(), null, // getMaturityDate(),
                getOwnerCodeID(), getAmount() };
    }

}
