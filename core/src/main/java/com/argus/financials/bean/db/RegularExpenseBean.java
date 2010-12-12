/*
 * RegularExpenseBean.java
 *
 * Created on 6 November 2001, 16:03
 */

package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.bean.Financial;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.bean.RegularExpense;

public class RegularExpenseBean extends RegularBean {

    /** Creates new RegularExpenseBean */
    public RegularExpenseBean() {
    }

    public RegularExpenseBean(RegularExpense value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return RegularExpense.class;
    }

    public Financial getNewFinancial() {
        return new RegularExpense();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.REGULAR_EXPENSE;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_REGULAREXPENCE;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = super.store(con);

        if (getPrimaryKeyID() == null || getPrimaryKeyID().intValue() < 0) {
            // then create link
            FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            // check its object type
            if (getClass().equals(RegularExpenseBean.class))
                setPrimaryKeyID(new Integer(primaryKeyID));

        }

        // check its object type
        if (getClass().equals(RegularExpenseBean.class))
            setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

}
