/*
 *  ClientServiceImpl.java
 *
 *  Created on 24 July 2001, 13:11
 */
package com.argus.financials.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.FinderException;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceException;
import com.argus.financials.strategy.StrategyGroup;
import com.argus.financials.strategy.db.StrategyGroupBean;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

/**
 * Description of the Class
 * 
 * @author Valeri Chibaev
 * @created 24 July 2001, 13:11
 */
public class ClientServiceImpl extends PersonServiceImpl implements ClientService {

    /*
     * 
     */
    private PersonServiceImpl partner;

    private boolean active;

    private java.util.Date reviewDate;

    private java.util.Date feeDate;

    // value has to be Integer
    public void setOwnerPrimaryKey(Object value) throws ServiceException {
        Integer id = value == null ? null : ((Number) value).intValue();
        if (equals(id, getOwnerPrimaryKeyID()))
            return;
        FPSLinkObject lo = FPSLinkObject.getInstance();
        try {
            Connection con = getConnection();
            lo.unlink(getOwnerPrimaryKeyID(), getPrimaryKeyID(), USER_2_CLIENT, con);
            if (id != null)
                lo.link(id, getPrimaryKeyID(), USER_2_CLIENT, con);
            setOwnerPrimaryKeyID(id);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean validatePassword(String password) throws ServiceException {
        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;
        boolean validate = false;
        try {
            con = this.getConnection();
            sql = con.prepareStatement(
            // "SELECT ClientPersonID FROM ClientPerson WHERE ( ClientPersonID =
            // ?)"
                    "Select LoginName from UserPerson where"
                            + " ( UserPersonID = ?)"
                            + " AND ( LoginPassword = ? ) ");
            int i = 0;
            sql.setInt(++i, getOwnerPrimaryKeyID().intValue());
            sql.setString(++i, password);

            rs = sql.executeQuery();

            if (rs.next())
                validate = true;

            close(rs, sql);
            rs = null;
            sql = null;

            return validate;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }

    }

    /**
     * Description of the Method
     * 
     * @return Description of the Return Value
     * @exception ServiceException
     *                Description of the Exception
     * @exception CreateException
     *                Description of the Exception
     */
    public Integer create() throws ServiceException, CreateException {

        PreparedStatement sql = null;
        try {
            Connection con = this.getConnection();
            // get new ObjectID for client
            Integer personID = new Integer(getNewObjectID(CLIENT_PERSON, con));
            // add to person table
            sql = con.prepareStatement("INSERT INTO person (PersonID) VALUES (?)");
            sql.setInt(1, personID.intValue());
            sql.executeUpdate();
            // add to ClientPerson table
            sql = con.prepareStatement("INSERT INTO ClientPerson (ClientPersonID) VALUES (?)");
            sql.setInt(1, personID.intValue());
            sql.executeUpdate();
            // link user and client
            if (getOwnerPrimaryKeyID() != null)
                FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID(),
                        personID, USER_2_CLIENT, con);
            this.setPrimaryKeyID(personID);
            return personID;
        } catch (SQLException e) {
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(null, sql);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * Description of the Method
     * 
     * @exception Throwable
     *                Description of the Exception
     */
    protected void finalize() throws Throwable {

        super.finalize();
    }

    /**
     * Description of the Method
     * 
     * @param personID
     *            Description of the Parameter
     * @return Description of the Return Value
     * @exception ServiceException
     *                Description of the Exception
     * @exception FinderException
     *                Description of the Exception
     */
    public Integer findByPrimaryKey(Integer personID) throws ServiceException,
            FinderException {

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.getConnection();

            // sql = con.
            sql = con.prepareStatement(
            // "SELECT ClientPersonID FROM ClientPerson WHERE ( ClientPersonID =
            // ?)"
                    "SELECT ObjectID1, Active, FeeDate, ReviewDate"
                            + " FROM Link l, ClientPerson c"
                            + " WHERE"
                            + " ( ClientPersonID = ?)"
                            + " AND ( l.LinkObjectTypeID = ? ) AND ( l.ObjectID2 = c.ClientPersonID )"
                            + " AND ( l.LogicallyDeleted IS NULL )");

            int i = 0;
            sql.setInt(++i, personID.intValue());
            sql.setInt(++i, USER_2_CLIENT); // 2003

            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find client ID: "
                        + personID);

            Integer ownerPrimaryKeyID = (Integer) rs.getObject(1); // user

            String s = rs.getString(2);
            if (s == null)
                active = false;
            else if (BooleanCode.rcNO.getCode().compareToIgnoreCase(s) == 0)
                active = false;
            else if (BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0)
                active = true;

            setFeeDate(rs.getDate("FeeDate"));
            setReviewDate(rs.getDate("ReviewDate"));

            close(rs, sql);
            rs = null;
            sql = null;

            // we need client personal information
            load(personID, con);

            // setPrimaryKeyID( personID );
            setOwnerPrimaryKeyID(ownerPrimaryKeyID);

            return personID;
        } catch (ObjectNotFoundException e) {
            throw new FinderException(e);
        } catch (SQLException sqle) {
            throw new FinderException(sqle);
        } finally {
            try {
                close(rs, sql);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }

    }

    /*
     * get/set
     */
    public boolean isActive() throws ServiceException {
        return active;
    }

    public void setActive(boolean value) throws ServiceException {
        if (value == active)
            return;

        active = value;
        setModified(true);
    }

    public java.util.Date getFeeDate() throws ServiceException {
        return feeDate;
    }

    public void setFeeDate(java.util.Date value)
            throws ServiceException {
        if (equals(feeDate, value))
            return;

        feeDate = value;
        setModified(true);
    }

    public java.util.Date getReviewDate() throws ServiceException {
        return reviewDate;
    }

    public void setReviewDate(java.util.Date value)
            throws ServiceException {
        if (equals(reviewDate, value))
            return;

        reviewDate = value;
        setModified(true);
    }

    /**
     * Gets the partner attribute of the ClientServiceImpl object
     * 
     * @param create
     *            Description of the Parameter
     * @return The partner value
     * @exception ServiceException
     *                Description of the Exception
     */
    public PersonService getPartner(boolean create) throws ServiceException {

        if (partner == null) {
            Integer partnerID = getPartnerPrimaryKeyID();
            if (partnerID == null)
                create = getPersonName().isMarried();
            else
                create = true;

            if (create) {
                // will create new person if null
                partner = getPerson(partnerID);

                // store and link partner to client
                if (partnerID == null)
                    storePartner();
            }

        }

        return partner;

    }

    /**
     * @return The partnerPrimaryKeyID value
     * @exception ServiceException
     *                Description of the Exception
     */
    private Integer getPartnerPrimaryKeyID() throws ServiceException {

        if (partner != null)
            return partner.getPrimaryKeyID();

        Connection con = null;
        try {
            con = getConnection();
            List list = getLinkedObjects(CLIENT_2_PERSON, PARTNER,
                    CLIENT$PERSON_2_RELATIONSHIP_FINANCE, con);
            return list == null || list.size() == 0 ? null : (Integer) list
                    .get(list.size() - 1);
            // last one with max ID

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    /**
     * @return list of strategies
     * @exception ServiceException
     *                Description of the Exception
     */
    public java.util.Collection getStrategies() throws ServiceException {

        try {
            Connection con = getConnection();
            return getStrategies(con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private Collection getStrategies(Connection con)
            throws java.sql.SQLException, ServiceException {
        List list = getLinkedObjects(
                LinkObjectTypeConstant.PERSON_2_STRATEGYGROUP, con);
        if (list == null || list.size() == 0)
            return null;

        java.util.Collection strategies = null;
        StrategyGroupBean sgb = null;

        java.util.Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Integer strategyGroupID = (Integer) iter.next();

            if (strategies == null)
                strategies = new java.util.ArrayList();

            StrategyGroup strategy = new StrategyGroup();
            strategy.setPrimaryKeyID(strategyGroupID);
            strategy.setOwnerPrimaryKeyID(getPrimaryKeyID());

            if (sgb == null)
                sgb = new StrategyGroupBean();
            sgb.setStrategyGroup(strategy);

            try {
                sgb.update(con);
            } catch (ObjectNotFoundException e) {
                System.err.println(e.getMessage());
                continue;
            }

            strategies.add(strategy);

        }

        return strategies;

    }

    public void storeStrategy(StrategyGroup strategy) throws ServiceException {
        if (strategy == null)
            return;
        try {
            Connection con = getConnection();
            StrategyGroupBean sgb = new StrategyGroupBean(strategy);
            sgb.setOwnerPrimaryKeyID(getPrimaryKeyID());
            sgb.store(con);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteStrategy(StrategyGroup strategy) throws ServiceException {
        if (strategy == null || strategy.getPrimaryKeyID() == null)
            return;
        try {
            Connection con = getConnection();
            FPSLinkObject.getInstance().unlink(getPrimaryKeyID(),
                    strategy.getPrimaryKeyID(),
                    LinkObjectTypeConstant.PERSON_2_STRATEGYGROUP, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void implementStrategy(StrategyGroup strategy)
            throws ServiceException {
        if (strategy == null || strategy.getPrimaryKeyID() == null)
            return;
        getFinancials(); // refresh ( for deleteFinancials( true, con ) )
        try {
            Connection con = getConnection();
            // delete all current financials
            deleteFinancials(con);
            implementStrategy(strategy, con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    private void implementStrategy(StrategyGroup strategy, Connection con)
            throws SQLException {

        // get strategy restructured financials
        // deep copy (we do not want to modify strategy data), set to current
        // assets, PrimaryKeyID(s) null
        Map financials = strategy.getRestructureFinancials(true);

        // store strategy financials as new current set
        setFinancials(financials);
        storeFinancials(con);

    }

    public void rollbackStrategy(StrategyGroup strategy)
            throws ServiceException {
        if (strategy == null || strategy.getPrimaryKeyID() == null)
            return;
        try {
            Connection con = getConnection();
            // delete all current financials
            deleteFinancials(con);
            rollbackStrategy(strategy, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void rollbackStrategy(StrategyGroup strategy, Connection con)
            throws SQLException {

        // get strategy restructured financials
        // deep copy, PrimaryKeyID(s) already null
        Map financials = strategy.getCollectionFinancials(true);

        // store strategy financials as new current set
        setFinancials(financials);
        System.out.println(financials);
        storeFinancials(con);

    }

    public int storePerson(Connection con) throws SQLException {

        // will be created if nessesary
        int personID = super.storePerson(con);

        PreparedStatement sql = con.prepareStatement("UPDATE ClientPerson SET"
                + " Active=?," + " FeeDate=?," + " ReviewDate=?"
                + " WHERE ClientPersonID=?", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

        sql.setString(1, active ? BooleanCode.rcYES.getCode()
                : BooleanCode.rcNO.getCode());

        if (feeDate == null)
            sql.setNull(2, java.sql.Types.VARCHAR);
        else
            sql.setString(2, DateTimeUtils.getJdbcDate(feeDate));

        if (reviewDate == null)
            sql.setNull(3, java.sql.Types.VARCHAR);
        else
            sql.setString(3, DateTimeUtils.getJdbcDate(reviewDate));

        sql.setInt(4, personID);

        sql.executeUpdate();

        close(null, sql);

        return personID;

    }

    /**
     * Description of the Method
     * 
     * @exception ServiceException
     *                Description of the Exception
     */
    private void storePartner() throws ServiceException {
        try {
            Connection con = getConnection();
            storePartner(con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Description of the Method
     * 
     * @param con
     *            Description of the Parameter
     * @exception SQLException
     *                Description of the Exception
     */
    private void storePartner(Connection con) throws SQLException {

        if (partner == null || !partner.isModified())
            return;

        partner.setOwnerPrimaryKeyID(getPrimaryKeyID());
        partner.storePerson(con);
        partner.setLink(CLIENT_2_PERSON, PARTNER,
                CLIENT$PERSON_2_RELATIONSHIP_FINANCE, con);

    }

    /***************************************************************************
     * Category
     **************************************************************************/
    public java.util.Collection getCategories() throws ServiceException {

        try {
            Connection con = getConnection();
            return getCategories(con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private java.util.Collection getCategories(Connection con)
            throws java.sql.SQLException, ServiceException {

        ResultSet rs = null;
        PreparedStatement sql = con.prepareStatement(
                "SELECT CategoryID, CategoryName, CategoryDesc"
                        + " FROM Category", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

        java.util.Collection categories = null;
        ReferenceCode category;

        try {
            rs = sql.executeQuery();
            if (categories == null)
                categories = new java.util.ArrayList();
            while (rs.next()) {
                category = new ReferenceCode(new Integer(rs.getInt(1)));
                category.setCodeDesc(rs.getString(2)); // PlanDataDesc
                category.setObject(rs.getString(3)); // PlanDataText
                categories.add(category);
            }
        } finally {
            close(rs, sql);
        }
        Collections.sort((java.util.ArrayList) categories, new Comparator() {
            public int compare(Object o1, Object o2) {
                String d1 = ((ReferenceCode) o1).getCodeDesc();
                String d2 = ((ReferenceCode) o2).getCodeDesc();
                return (d1.toLowerCase().compareTo(d2.toLowerCase()));
            }
        });
        return categories;

    }

    public java.util.Collection getSelectedCategories() throws ServiceException {

        try {
            Connection con = getConnection();
            return getSelectedCategories(con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private java.util.Collection getSelectedCategories(Connection con)
            throws java.sql.SQLException, ServiceException {

        ResultSet rs = null;
        PreparedStatement sql = con
                .prepareStatement(
                        "SELECT Category.CategoryID, CategoryName, CategoryDesc"
                                + " FROM Category, SelectedCategory"
                                + " WHERE SelectedCategory.personID = "
                                + getPersonID()
                                + " AND SelectedCategory.CategoryID = Category.CategoryID",
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        java.util.Collection categories = null;
        ReferenceCode category;

        try {
            rs = sql.executeQuery();
            if (categories == null)
                categories = new java.util.ArrayList();
            while (rs.next()) {
                category = new ReferenceCode(new Integer(rs.getInt(1)));
                category.setCodeDesc(rs.getString(2)); // PlanDataDesc
                category.setObject(rs.getString(3)); // PlanDataText
                categories.add(category);
            }
        } finally {
            close(rs, sql);
        }
        Collections.sort((java.util.ArrayList) categories, new Comparator() {
            public int compare(Object o1, Object o2) {
                String d1 = ((ReferenceCode) o1).getCodeDesc();
                String d2 = ((ReferenceCode) o2).getCodeDesc();
                return (d1.toLowerCase().compareTo(d2.toLowerCase()));
            }
        });
        return categories;

    }

    public void addCategory(com.argus.util.ReferenceCode category)
            throws ServiceException {

        if (category == null || category.getCodeDesc() == null)
            return;

        String desc = category.getCodeDesc().trim();
        if (desc.length() > 50) {
            desc = desc.substring(0, 50);
            category.setCodeDesc(desc);
        }

        try {
            Connection con = getConnection();
            addCategory(category, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private void addCategory(com.argus.util.ReferenceCode category,
            Connection con) throws java.sql.SQLException, ServiceException {
        PreparedStatement sql = null;
        try {
            // do insert into Person table
            sql = con.prepareStatement("INSERT INTO Category"
                    + " (CategoryName, CategoryDesc)" + " VALUES" + " (?,?)");

            int i = 0;
            sql.setString(++i, category.getCodeDesc());
            sql.setString(++i, category.getObject() == null ? "" : category
                    .getObject().toString());

            sql.executeUpdate();

        } finally {
            close(null, sql);
        }

    }

    public boolean removeCategory(ReferenceCode category)
            throws ServiceException {
        if (category == null)
            return true;

        try {
            Connection con = getConnection();
            return removeCategory(category, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private boolean removeCategory(ReferenceCode category, Connection con)
            throws java.sql.SQLException, ServiceException {
        Statement stmt = null;
        try {
            // do insert into Category table
            int id = category.getCodeID();
            stmt = con.createStatement();
            String sql = "SELECT * from SelectedCategory WHERE CategoryID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                return false;
            sql = "DELETE from Category " + "WHERE CategoryID = " + id;
            stmt.executeUpdate(sql);
        } finally {
            close(null, stmt);
        }
        return true;
    }

    public void updateCategory(com.argus.util.ReferenceCode category)
            throws ServiceException {
        if (category == null)
            return;
        try {
            Connection con = getConnection();
            updateCategory(category, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void updateCategory(ReferenceCode category, Connection con)
            throws java.sql.SQLException, ServiceException {
        Statement stmt = null;
        try {
            // do insert into Category table
            stmt = con.createStatement();
            String sql = "UPDATE Category SET" + " CategoryName = '"
                    + category.getCodeDesc() + "'" + " WHERE CategoryID = "
                    + category.getCodeID();
            stmt.executeUpdate(sql);
        } finally {
            close(null, stmt);
        }
    }

    public void addSelectedCategories(java.util.Vector selectedCategories)
            throws ServiceException {
        if (selectedCategories == null)
            return;
        try {
            Connection con = getConnection();
            addSelectedCategories(selectedCategories, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void addSelectedCategories(java.util.Vector selectedCategories,
            Connection con) throws java.sql.SQLException, ServiceException {
        Statement stmt = null;
        try {
            // do insert into Category table
            int id = getPersonID();
            stmt = con.createStatement();
            stmt.execute("Delete from SelectedCategory where PersonID = " + id);
            Iterator iter = selectedCategories.iterator();
            while (iter.hasNext()) {
                String sql = "INSERT INTO SelectedCategory"
                        + " (CategoryID, PersonID)" + " VALUES" + " ("
                        + iter.next() + ", " + id + ")";
                stmt.addBatch(sql);
            }
            int[] results = stmt.executeBatch();
            stmt.clearBatch();
        } finally {
            close(null, stmt);
        }
    }

}