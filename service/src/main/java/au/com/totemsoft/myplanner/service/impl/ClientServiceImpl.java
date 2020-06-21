/*
 *  ClientServiceImpl.java
 *
 *  Created on 24 July 2001, 13:11
 */
package au.com.totemsoft.myplanner.service.impl;

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

import javax.inject.Inject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IClient;
import au.com.totemsoft.myplanner.api.bean.IStrategyGroup;
import au.com.totemsoft.myplanner.api.bean.IUser;
import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.code.BooleanCode;
import au.com.totemsoft.myplanner.dao.ClientDao;
import au.com.totemsoft.myplanner.dao.PersonDao;
import au.com.totemsoft.myplanner.domain.dto.ClientDto;
import au.com.totemsoft.myplanner.domain.hibernate.Client;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.CreateException;
import au.com.totemsoft.myplanner.service.FinderException;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.strategy.StrategyGroup;
import au.com.totemsoft.myplanner.strategy.db.StrategyGroupBean;
import au.com.totemsoft.util.DateTimeUtils;
import au.com.totemsoft.util.ReferenceCode;

/**
 * Description of the Class
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @created 24 July 2001, 13:11
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ClientServiceImpl extends PersonServiceImpl implements ClientService {

    @Inject private ClientDao clientDao;

    @Inject private PersonDao personDao;

    private PersonServiceImpl partner;

    private boolean active;

    private java.util.Date reviewDate;

    private java.util.Date feeDate;

    @Override
    public IClient findClientById(Long clientId) {
        // TODO Auto-generated method stub
        return clientDao.findById(clientId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void setOwnerPrimaryKey(Object value) throws ServiceException {
        // value has to be Integer
        Integer id = value == null ? null : ((Number) value).intValue();
        if (equals(id, getOwnerId()))
            return;
        try (Connection con = sqlHelper.getConnection();) {
            linkObjectDao.unlink(getOwnerId(), getId(), USER_2_CLIENT, con);
            if (id != null)
                linkObjectDao.link(id, getId(), USER_2_CLIENT, con);
            setOwnerId(id);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean validatePassword(String password) throws ServiceException {
        PreparedStatement sql = null;
        ResultSet rs = null;
        boolean validate = false;
        try (Connection con = sqlHelper.getConnection();) {
            sql = con.prepareStatement(
            // "SELECT ClientPersonID FROM ClientPerson WHERE ( ClientPersonID =
            // ?)"
                    "Select LoginName from UserPerson where"
                            + " ( UserPersonID = ?)"
                            + " AND ( LoginPassword = ? ) ");
            int i = 0;
            sql.setInt(++i, getOwnerId().intValue());
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Long createClient() throws ServiceException, CreateException {
        final IUser user = userPreferences.getUser();
        final Integer ownerId = user.getId().intValue();

        PreparedStatement sql = null;
        try (Connection con = sqlHelper.getConnection();) {
            // get new ObjectID for client
            Integer personId = getNewObjectID(CLIENT_PERSON, con);

            // add to Person table
//            Person p = new Person();
//            p.setId(personId.longValue());
//            personDao.persist(p);
//            personDao.flushAndClear();
            sql = con.prepareStatement("INSERT INTO Person (PersonID) VALUES (?)");
            sql.setInt(1, personId);
            sql.executeUpdate();
            LOG.info("createClient: person #" + personId);

            // add to ClientPerson table
//            Client c = new Client();
//            c.setId(personId.longValue());
//            clientDao.persist(c);
//            clientDao.flushAndClear();
            sql = con.prepareStatement("INSERT INTO ClientPerson (ClientPersonID) VALUES (?)");
            sql.setInt(1, personId);
            sql.executeUpdate();
            LOG.info("createClient: client #" + personId);

            // link user and client
            if (ownerId != null) {
                linkObjectDao.link(ownerId, personId, USER_2_CLIENT, con);
            }
            setId(personId);

            //
            return personId.longValue();
        } catch (SQLException e) {
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            try {
                close(null, sql);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void saveClient(ClientDto client) {
        Client c = clientDao.findById(client.getId().longValue());
        if (c != null) {
            
            LOG.info("saveClient: " + client);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void removeClient(ClientDto client) {
        Client c = clientDao.findById(client.getId().longValue());
        if (c != null) {
            //c.setActive(false);
            clientDao.remove(c);
            LOG.info("removeClient: " + client);
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
        PreparedStatement sql = null;
        ResultSet rs = null;
        try (Connection con = sqlHelper.getConnection();) {
            sql = con.prepareStatement(
            // "SELECT ClientPersonID FROM ClientPerson WHERE ( ClientPersonID = ?)"
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

            // setId( personID );
            setOwnerId(ownerPrimaryKeyID);

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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
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
            return partner.getId();

        try (Connection con = sqlHelper.getConnection();) {
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
    public Collection getStrategies() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            Collection result = getStrategies(con);
            return result;
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
            strategy.setId(strategyGroupID);
            strategy.setOwnerId(getId());

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void storeStrategy(IStrategyGroup strategy) throws ServiceException {
        if (strategy == null)
            return;
        try (Connection con = sqlHelper.getConnection();) {
            StrategyGroupBean sgb = new StrategyGroupBean(strategy);
            sgb.setOwnerId(getId());
            sgb.store(con);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void deleteStrategy(IStrategyGroup strategy) throws ServiceException {
        if (strategy == null || strategy.getId() == null)
            return;
        try (Connection con = sqlHelper.getConnection();) {
            linkObjectDao.unlink(getId(),
                    strategy.getId(),
                    LinkObjectTypeConstant.PERSON_2_STRATEGYGROUP, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void implementStrategy(IStrategyGroup strategy) throws ServiceException {
        if (strategy == null || strategy.getId() == null)
            return;
        try {
            // delete all current financials
            deleteFinancials();
            // get strategy restructured financials
            // deep copy (we do not want to modify strategy data), set to current assets, PrimaryKeyID(s) null
            Map financials = strategy.getRestructureFinancials(true);
            // store strategy financials as new current set
            storeFinancials(financials, this.getFinancialGoal());
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void rollbackStrategy(IStrategyGroup strategy) throws ServiceException {
        if (strategy == null || strategy.getId() == null)
            return;
        try {
            // delete all current financials
            deleteFinancials();
            // get strategy restructured financials deep copy, PrimaryKeyID(s) already null
            Map financials = strategy.getCollectionFinancials(true);
            // store strategy financials as new current set
            storeFinancials(financials, this.getFinancialGoal());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
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
        try (Connection con = sqlHelper.getConnection();) {
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

        partner.setOwnerId(getId());
        partner.storePerson(con);
        partner.setLink(CLIENT_2_PERSON, PARTNER,
                CLIENT$PERSON_2_RELATIONSHIP_FINANCE, con);

    }

    /***************************************************************************
     * Category
     **************************************************************************/
    public Collection getCategories() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            Collection result = getCategories(con);
            return result;
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
                category.setDescription(rs.getString(2)); // PlanDataDesc
                category.setObject(rs.getString(3)); // PlanDataText
                categories.add(category);
            }
        } finally {
            close(rs, sql);
        }
        Collections.sort((java.util.ArrayList) categories, new Comparator() {
            public int compare(Object o1, Object o2) {
                String d1 = ((ReferenceCode) o1).getDescription();
                String d2 = ((ReferenceCode) o2).getDescription();
                return (d1.toLowerCase().compareTo(d2.toLowerCase()));
            }
        });
        return categories;

    }

    public Collection getSelectedCategories() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            Collection result = getSelectedCategories(con);
            return result;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private Collection getSelectedCategories(Connection con)
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
                category.setDescription(rs.getString(2)); // PlanDataDesc
                category.setObject(rs.getString(3)); // PlanDataText
                categories.add(category);
            }
        } finally {
            close(rs, sql);
        }
        Collections.sort((java.util.ArrayList) categories, new Comparator() {
            public int compare(Object o1, Object o2) {
                String d1 = ((ReferenceCode) o1).getDescription();
                String d2 = ((ReferenceCode) o2).getDescription();
                return (d1.toLowerCase().compareTo(d2.toLowerCase()));
            }
        });
        return categories;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void addCategory(au.com.totemsoft.util.ReferenceCode category)
            throws ServiceException {

        if (category == null || category.getDescription() == null)
            return;

        String desc = category.getDescription().trim();
        if (desc.length() > 50) {
            desc = desc.substring(0, 50);
            category.setDescription(desc);
        }

        try (Connection con = sqlHelper.getConnection();) {
            addCategory(category, con);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private void addCategory(au.com.totemsoft.util.ReferenceCode category,
            Connection con) throws SQLException, ServiceException {
        PreparedStatement sql = null;
        try {
            // do insert into Person table
            sql = con.prepareStatement("INSERT INTO Category"
                    + " (CategoryName, CategoryDesc)" + " VALUES" + " (?,?)");

            int i = 0;
            sql.setString(++i, category.getDescription());
            sql.setString(++i, category.getObject() == null ? "" : category
                    .getObject().toString());

            sql.executeUpdate();

        } finally {
            close(null, sql);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public boolean removeCategory(ReferenceCode category)
            throws ServiceException {
        if (category == null)
            return true;

        try (Connection con = sqlHelper.getConnection();) {
            boolean result = removeCategory(category, con);
            return result;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private boolean removeCategory(ReferenceCode category, Connection con)
            throws java.sql.SQLException, ServiceException {
        Statement stmt = null;
        try {
            // do insert into Category table
            int id = category.getId();
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void updateCategory(au.com.totemsoft.util.ReferenceCode category)
            throws ServiceException {
        if (category == null)
            return;
        try (Connection con = sqlHelper.getConnection();) {
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
                    + category.getDescription() + "'" + " WHERE CategoryID = "
                    + category.getId();
            stmt.executeUpdate(sql);
        } finally {
            close(null, stmt);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void addSelectedCategories(java.util.Vector selectedCategories)
            throws ServiceException {
        if (selectedCategories == null)
            return;
        try (Connection con = sqlHelper.getConnection();) {
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
