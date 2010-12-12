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
import com.argus.financials.bean.TaxOffset;

public class TaxOffsetBean extends RegularBean {

    /** Creates new TaxOffsetBean */
    public TaxOffsetBean() {
    }

    public TaxOffsetBean(TaxOffset value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return TaxOffset.class;
    }

    public Financial getNewFinancial() {
        return new TaxOffset();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.TAX_OFFSET;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_TAXOFFSET;
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
            if (getClass().equals(TaxOffsetBean.class))
                setPrimaryKeyID(new Integer(primaryKeyID));

        }

        // check its object type
        if (getClass().equals(TaxOffsetBean.class))
            setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

}
