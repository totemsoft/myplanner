/*
 * LiabilityBean.java
 *
 * Created on 6 November 2001, 16:04
 */

package com.argus.financials.bean.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.Liability;

public class LiabilityBean extends RegularBean {

    /** Creates new LiabilityBean */
    public LiabilityBean() {
    }

    public LiabilityBean(Liability value) {
        super(value);
    }

    /**
     * helper methods
     */
    public Class getFinancialClass() {
        return Liability.class;
    }

    public Financial getNewFinancial() {
        return new Liability();
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.LIABILITY;
    }

    private int getLinkObjectTypeID() throws SQLException {
        switch (objectTypeID1) {
        case ObjectTypeConstant.PERSON:
            return LinkObjectTypeConstant.PERSON_2_LIABILITY;
        default:
            throw new SQLException("Unknown objectTypeID1: " + objectTypeID1);
        }
    }

    protected String getSelectFieldsList() {
        return "AccountNumber, InterestRate, " + super.getSelectFieldsList();
    }

    /**
     * IPersistable methods
     */
    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {

            sql = con
                    .prepareStatement(
                            "SELECT "
                                    + getSelectFieldsList()
                                    + " FROM Liability, Regular, Financial"
                                    + " WHERE (LiabilityID = ?)"
                                    + " AND (LiabilityID = RegularID) AND (RegularID = FinancialID) AND (NextID IS NULL)",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Liability ID: "
                        + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setId(primaryKeyID);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        // Liability table
        setAccountNumber(rs.getString("AccountNumber"));
        setInterestRate(new Double(rs.getDouble("InterestRate")));

        // Regular/Financial tables
        super.load(rs);

    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = super.store(con);

        int i = 0;
        PreparedStatement sql = null;

        try {
            if (getId() == null || getId().intValue() < 0) {

                // do insert into Liability table
                sql = con.prepareStatement("INSERT INTO Liability"
                        + " (LiabilityID, AccountNumber, InterestRate)"
                        + " VALUES" + " (?,?,?)");

                sql.setInt(++i, primaryKeyID);
                sql.setString(++i, getAccountNumber());
                // sql.setObject( ++i, getInterestRate(), java.sql.Types.DOUBLE
                // );
                if (getInterestRate() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getInterestRate().doubleValue());

                sql.executeUpdate();

                // then create link
                linkObjectDao.link(
                        getOwnerId().intValue(), primaryKeyID,
                        getLinkObjectTypeID(), con);

                setId(new Integer(primaryKeyID));

            } else {

                // do update on RegularIncome table
                sql = con.prepareStatement("UPDATE Liability SET"
                        + " AccountNumber=?, InterestRate=?"
                        + " WHERE LiabilityID=?");

                sql.setString(++i, getAccountNumber());
                // sql.setObject( ++i, getInterestRate(), java.sql.Types.DOUBLE
                // );
                if (getInterestRate() == null)
                    sql.setNull(++i, java.sql.Types.DECIMAL);
                else
                    sql.setDouble(++i, getInterestRate().doubleValue());
                sql.setInt(++i, primaryKeyID);

                sql.executeUpdate();

            }
        } finally {
            close(null, sql);
        }

        setModified(false);

        return primaryKeyID;

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public String getAccountNumber() {
        return ((Liability) getFinancial()).getAccountNumber();
    }

    public void setAccountNumber(String value) {
        ((Liability) getFinancial()).setAccountNumber(value);
    }

    public Double getInterestRate() {
        return ((Liability) getFinancial()).getInterestRate();
    }

    public void setInterestRate(Double value) {
        ((Liability) getFinancial()).setInterestRate(value);
    }

}
