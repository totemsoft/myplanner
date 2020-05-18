/*
 * StrategyGroupBean.java
 *
 * Created on 17 December 2002, 17:34
 */

package com.argus.financials.strategy.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.bean.IStrategyGroup;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.util.DateTimeUtils;
import com.argus.util.StringUtils;

public class StrategyGroupBean extends AbstractPersistable {

    private IStrategyGroup strategy;

    /** Creates a new instance of StrategyGroupBean */
    public StrategyGroupBean() {
    }

    public StrategyGroupBean(IStrategyGroup strategy) {
        this.strategy = strategy;
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.STRATEGYGROUP;
    }

    private int getLinkObjectTypeID() {
        return LinkObjectTypeConstant.PERSON_2_STRATEGYGROUP;
    }

    public IStrategyGroup getStrategyGroup() {
        return strategy;
    }

    public void setStrategyGroup(IStrategyGroup strategy) {
        this.strategy = strategy;
    }

    public Integer getId() {
        return strategy.getId();
    }

    public void setId(Integer value) {
        strategy.setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return strategy.getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        strategy.setOwnerId(value);
    }

    public String getSelectFieldsList() {
        return "StrategyGroupID, StrategyGroupTitle, StrategyGroupData2, DateCreated, DateModified";
    }

    public void update(Connection con) throws SQLException,
            com.argus.financials.api.ServiceException, ObjectNotFoundException {
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {

            sql = con.prepareStatement("SELECT " + getSelectFieldsList()
                    + " FROM StrategyGroup WHERE ( StrategyGroupID = ? )");

            sql.setInt(1, getId().intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                        "Can not find StrategyGroup: " + strategy);

            load(rs);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        int i = 0;

        Integer strategyGroupID = (Integer) rs.getObject(++i);
        if (!equals(getId(), strategyGroupID))
            setId(strategyGroupID);

        strategy.setStrategyGroupDesc(rs.getString(++i));

        String sData2 = rs.getString(++i); // text presentation of binary
        strategy.setSerializedStrategyGroupData(StringUtils.fromHexString(sData2));

        strategy.setDateCreated(rs.getDate(++i));
        strategy.setDateModified(rs.getDate(++i)); // has to be last

    }

    public int store(Connection con) throws SQLException {

        int i = 0;
        Integer primaryKeyID = getId();
        PreparedStatement sql = null;

        try {
            if (primaryKeyID == null || primaryKeyID.intValue() < 0) {

                primaryKeyID = new Integer(getNewObjectID(getObjectTypeID(),
                        con));
                strategy.setId(primaryKeyID);

                // do insert into table
                sql = con
                        .prepareStatement("INSERT INTO StrategyGroup ("
                                + "StrategyGroupID, StrategyGroupTitle, DateModified"
                                + ", StrategyGroupData2"
                                + ") VALUES ("
                                + "?,?,?"
                                + ",?"
                                + ")");

                sql.setObject(++i, primaryKeyID, java.sql.Types.INTEGER);
                sql.setString(++i, strategy.getStrategyGroupDesc());

                strategy.setDateCreated(new java.util.Date());
                strategy.setDateModified(strategy.getDateCreated());
                sql.setString(++i, DateTimeUtils.getJdbcDate(strategy
                        .getDateModified()));

                byte[] sData = strategy.getSerializedStrategyGroupData();
                sql.setString(++i, StringUtils.toHexString(sData));

                sql.executeUpdate();

                // then create link
                linkObjectDao.link(getOwnerId(),
                        primaryKeyID, getLinkObjectTypeID(), con);

            } else {

                // do update on table
                sql = con
                        .prepareStatement("UPDATE StrategyGroup SET"
                                + " StrategyGroupTitle=?,DateModified=?,StrategyGroupData=?,StrategyGroupData2=?"
                                + " WHERE StrategyGroupID=?");

                sql.setString(++i, strategy.getStrategyGroupDesc());

                strategy.setDateModified(new java.util.Date());
                sql.setString(++i, DateTimeUtils.getJdbcDate(strategy
                        .getDateModified()));

                byte[] sData = strategy.getSerializedStrategyGroupData();
                sql.setBytes(++i, sData); // set it to null later ???
                sql.setString(++i, StringUtils.toHexString(sData));

                sql.setObject(++i, primaryKeyID, java.sql.Types.INTEGER);

                sql.executeUpdate();

            }

        } catch (java.io.IOException e) {
            throw new SQLException(e.getMessage());
        } finally {
            close(null, sql);
        }

        return primaryKeyID.intValue();

    }

}
