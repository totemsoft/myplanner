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
import au.com.totemsoft.myplanner.bean.TaxOffset;

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

        if (getId() == null || getId().intValue() < 0) {
            // then create link
            linkObjectDao.link(getOwnerId().intValue(),
                    primaryKeyID, getLinkObjectTypeID(), con);

            // check its object type
            if (getClass().equals(TaxOffsetBean.class))
                setId(new Integer(primaryKeyID));

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
