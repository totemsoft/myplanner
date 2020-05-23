/*
 * RegularExpenseBean.java
 *
 * Created on 6 November 2001, 16:03
 */

package au.com.totemsoft.myplanner.bean.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.RegularExpense;

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

        if (getId() == null || getId().intValue() < 0) {
            // then create link
            linkObjectDao.link(getOwnerId().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            // check its object type
            if (getClass().equals(RegularExpenseBean.class))
                setId(new Integer(primaryKeyID));

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
