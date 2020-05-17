/*
 * RegularIncomeBean.java
 *
 * Created on 6 November 2001, 16:03
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.RegularIncome;

public class RegularIncomeBean extends RegularBean {

    /** Creates new RegularIncomeBean */
    public RegularIncomeBean() {
    }

    public RegularIncomeBean(RegularIncome value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return RegularIncome.class;
    }

    public Financial getNewFinancial() {
        return new RegularIncome();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.REGULAR_INCOME;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_REGULARINCOME;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = super.store(con);

        if (getId() == null || getId().intValue() < 0) {

            // then create link
            linkObjectDao.link(getOwnerId().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            if (getClass().equals(RegularIncomeBean.class))
                setId(new Integer(primaryKeyID));

        }

        if (getClass().equals(RegularIncomeBean.class))
            setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

}
