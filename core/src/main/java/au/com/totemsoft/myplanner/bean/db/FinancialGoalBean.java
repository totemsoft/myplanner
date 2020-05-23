/*
 * FinancialGoalBean.java
 *
 * Created on 15 November 2001, 14:57
 */

package au.com.totemsoft.myplanner.bean.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.bean.FinancialGoal;

public class FinancialGoalBean extends AbstractPersistable {

    private FinancialGoal financialGoal; // aggregation

    // First level of linkage (e.g. objectTypeID1 = PERSON or BUSINESS),
    // objectTypeID2 = FINANCIAL_GOAL
    private int[] linkObjectTypes = new int[1]; // PERSON_2_FINANCIALGOAL

    /**
     * Creates new FinancialGoalBean
     */
    public FinancialGoalBean() {
    }

    public FinancialGoalBean(FinancialGoal value) {
        this.financialGoal = value;
    }

    /**
     * helper methods
     */
    public Class getCommentClass() {
        return FinancialGoal.class;
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.FINANCIAL_GOAL;
    }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        default:
            throw new RuntimeException(
                    "FinancialGoalBean.getLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    public void setLinkObjectTypeID(int level, int value) {
        switch (level) {
        case 1: {
            setModified(linkObjectTypes[0] != value);
            linkObjectTypes[0] = value;
            break;
        }
        default:
            throw new RuntimeException(
                    "FinancialGoalBean.setLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    /**
     * IPersistable methods
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {

            sql = con
                    .prepareStatement(
                            "SELECT GoalCodeID, TargetAge, TargetIncome, YearsIncomeReq, ResidualReq, DateCreated, LumpSumRequired, TargetStrategyID, Inflation, Notes"
                                    + " FROM FinancialGoal"
                                    + " WHERE (FinancialGoalID = ?)",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                        "Can not find FinancialGoal ID: " + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setId(primaryKeyID);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {

        try {
            setGoalCodeID((Integer) rs.getObject("GoalCodeID"));
        } catch (InvalidCodeException e) { /* imposible */
        }
        setTargetAge((Integer) rs.getObject("TargetAge"));
        setTargetIncome(rs.getBigDecimal("TargetIncome"));
        setYearsIncomeReq((Integer) rs.getObject("YearsIncomeReq"));
        setResidualReq(rs.getBigDecimal("ResidualReq"));
        setLumpSumRequired(rs.getBigDecimal("LumpSumRequired"));
        setInflation(rs.getBigDecimal("Inflation"));
        setTargetStrategyID((Integer) rs.getObject("TargetStrategyID"));
        setNotes(rs.getString("Notes"));
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = 0;

        int i = 0;
        PreparedStatement sql = null;

        try {
            if (getId() == null || getId().intValue() < 0) {

                primaryKeyID = getNewObjectID(getObjectTypeID(), con);

                // do insert into FinancialGoal table
                sql = con
                        .prepareStatement("INSERT INTO FinancialGoal"
                                + " (FinancialGoalID, GoalCodeID, TargetAge, TargetIncome, YearsIncomeReq, ResidualReq, LumpSumRequired, TargetStrategyID, Inflation, Notes)"
                                + " VALUES" + " (?,?,?,?,?,?,?,?,?,?)");

                sql.setInt(++i, primaryKeyID);
                sql.setObject(++i, getGoalCodeID(), java.sql.Types.INTEGER);
                sql.setObject(++i, getTargetAge(), java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getTargetIncome());
                sql.setObject(++i, getYearsIncomeReq(), java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getResidualReq());
                sql.setBigDecimal(++i, getLumpSumRequired());
                sql.setObject(++i, getTargetStrategyID(),
                        java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getInflation());
                sql.setString(++i, getNotes());

                sql.executeUpdate();

                // then create first level link
                int linkID = linkObjectDao.link(
                        getOwnerId().intValue(), primaryKeyID,
                        getLinkObjectTypeID(1), // PERSON_2_FINANCIALGOAL
                        con);

                setId(new Integer(primaryKeyID));

            } else {

                primaryKeyID = getId().intValue();

                // do update on FinancialGoal table
                sql = con
                        .prepareStatement("UPDATE FinancialGoal SET"
                                + " GoalCodeID=?, TargetAge=?, TargetIncome=?, YearsIncomeReq=?, ResidualReq=?, LumpSumRequired=?, TargetStrategyID=?, Inflation=?, Notes=?"
                                + " WHERE FinancialGoalID=?");

                sql.setObject(++i, getGoalCodeID(), java.sql.Types.INTEGER);
                sql.setObject(++i, getTargetAge(), java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getTargetIncome());
                sql.setObject(++i, getYearsIncomeReq(), java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getResidualReq());
                sql.setBigDecimal(++i, getLumpSumRequired());
                sql.setObject(++i, getTargetStrategyID(),
                        java.sql.Types.INTEGER);
                sql.setBigDecimal(++i, getInflation());
                sql.setString(++i, getNotes());
                sql.setInt(++i, primaryKeyID);
                sql.executeUpdate();

            }
        } finally {
            close(null, sql);
        }

        setModified(false);

        return primaryKeyID;

    }

    public void remove(Connection con) throws SQLException {

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public FinancialGoal getFinancialGoal() {
        if (financialGoal == null)
            financialGoal = new FinancialGoal();

        return financialGoal;
    }

    public void setFinancialGoal(FinancialGoal value) {

        // boolean modified = ;

        financialGoal = value;
        if (financialGoal != null)
            setModified(true);
    }

    public boolean isModified() {
        return getFinancialGoal().isModified();
    }

    public void setModified(boolean value) {
        getFinancialGoal().setModified(value);
    }

    public Integer getId() {
        return getFinancialGoal().getId();
    }

    public void setId(Integer value) {
        getFinancialGoal().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getFinancialGoal().getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getFinancialGoal().setOwnerId(value);
    }

    public Integer getGoalCodeID() {
        return getFinancialGoal().getGoalCodeID();
    }

    public void setGoalCodeID(Integer value) throws InvalidCodeException {
        getFinancialGoal().setGoalCodeID(value);
    }

    public Integer getTargetAge() {
        return getFinancialGoal().getTargetAge();
    }

    public void setTargetAge(Integer value) {
        getFinancialGoal().setTargetAge(value);
    }

    public java.math.BigDecimal getTargetIncome() {
        return getFinancialGoal().getTargetIncome();
    }

    public void setTargetIncome(java.math.BigDecimal value) {
        getFinancialGoal().setTargetIncome(value);
    }

    public java.math.BigDecimal getInflation() {
        return getFinancialGoal().getInflation();
    }

    public void setInflation(java.math.BigDecimal value) {
        getFinancialGoal().setInflation(value);
    }

    public Integer getTargetStrategyID() {
        return getFinancialGoal().getTargetStrategyID();
    }

    public void setTargetStrategyID(Integer value) {
        getFinancialGoal().setTargetStrategyID(value);
    }

    public java.math.BigDecimal getLumpSumRequired() {
        return getFinancialGoal().getLumpSumRequired();
    }

    public void setLumpSumRequired(java.math.BigDecimal value) {
        getFinancialGoal().setLumpSumRequired(value);
    }

    public String getNotes() {
        return getFinancialGoal().getNotes();
    }

    public void setNotes(String value) {
        getFinancialGoal().setNotes(value);
    }

    public Integer getYearsIncomeReq() {
        return getFinancialGoal().getYearsIncomeReq();
    }

    public void setYearsIncomeReq(Integer value) {
        getFinancialGoal().setYearsIncomeReq(value);
    }

    public java.math.BigDecimal getResidualReq() {
        return getFinancialGoal().getResidualReq();
    }

    public void setResidualReq(java.math.BigDecimal value) {
        getFinancialGoal().setResidualReq(value);
    }

}
