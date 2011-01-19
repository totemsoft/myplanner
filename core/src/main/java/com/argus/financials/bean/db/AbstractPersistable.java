package com.argus.financials.bean.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.argus.financials.bean.IPersistable;
import com.argus.financials.db.BaseSQLHelper;
import com.argus.financials.db.SQLServerHelper;
import com.argus.financials.etc.FPSObject;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.service.client.ObjectNotFoundException;

public abstract class AbstractPersistable extends FPSObject implements IPersistable {

    protected AbstractPersistable() {
        super();
    }

    protected AbstractPersistable(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * IPersistable methods
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {
    }

    public void load(ResultSet rs) throws SQLException {
    }

    public int store(Connection con) throws SQLException {
        return -1;
    }

    public Integer find() throws SQLException {
        return null;
    }

    public void remove(Connection con) throws SQLException {
    }

    /**
     * 
     */
    public BaseSQLHelper getHelper() throws SQLException {
        return SQLServerHelper.getInstance();
    }

    /**
     * 
     */
    public Connection getConnection() throws SQLException {
        return ServiceLocator.getInstance().getDataSource().getConnection();
    }

    public void close(ResultSet rs, Statement sql) throws SQLException {
        getHelper().close(rs, sql);
    }

    /**
     * 
     */
    public int getIdentityID(Connection con) throws SQLException {
        return getHelper().getIdentityID(con);
    }

    public int getNewObjectID(int objectTypeID, Connection con)
            throws SQLException {
        return getHelper().getNewObjectID(objectTypeID, con);
    }

    /**
     * 
     */
    /**
     * related/linked person(s)
     * 
     * linkObjectTypeID1: 1st level linkage (e.g. PERSON_2_PERSON,
     * CLIENT_2_PERSON, etc) objectID2: type of the linked person (PARTNER,
     * DEPENDENT, CONTACT, WILL_EXECUTOR, etc) linkObjectTypeID2: 2nd level
     * linkage (e.g. CLIENT$PERSON_2_RELATIONSHIP_FINANCE)
     */
    public int setLink(int linkObjectTypeID, Connection con)
            throws java.sql.SQLException {
        // first level link
        return FPSLinkObject.getInstance().link(getOwnerPrimaryKeyID(),
                getPrimaryKeyID(), linkObjectTypeID, con);

    }

    // second level link
    public int setLink(int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws java.sql.SQLException {

        int linkID = setLink(linkObjectTypeID1, con);

        linkID = FPSLinkObject.getInstance().link(linkID, objectID2,
                linkObjectTypeID2, con);

        return linkID;

    }

    public List getLinkedObjects(int linkObjectTypeID, Connection con)
            throws java.sql.SQLException {
        if (getPrimaryKeyID() == null)
            return null;
        return FPSLinkObject.getInstance().getLinkedObjects(
                getPrimaryKeyID().intValue(), linkObjectTypeID, con);

    }

    protected List getLinkedObjects(int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws java.sql.SQLException {
        if (getPrimaryKeyID() == null)
            return null;
        return FPSLinkObject.getInstance().getLinkedObjects(
                getPrimaryKeyID().intValue(), linkObjectTypeID1, objectID2,
                linkObjectTypeID2, con);

    }

    /**
     * HELPER METHODS
     */
    // Display an SQLException which has occured in this application.
    protected static void showSQLException(java.sql.SQLException e) {
        // Notice that a SQLException is actually a chain of SQLExceptions,
        // let's not forget to print all of them...
        java.sql.SQLException next = e;
        while (next != null) {
            System.err.println(next.getMessage());
            System.err.println("Error Code: " + next.getErrorCode());
            System.err.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }

}
