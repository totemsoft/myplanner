package au.com.totemsoft.myplanner.bean.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.dao.IPersistable;
import au.com.totemsoft.myplanner.api.dao.LinkObjectDao;
import au.com.totemsoft.myplanner.api.dao.ObjectDao;
import au.com.totemsoft.myplanner.api.service.FinancialService;
import au.com.totemsoft.myplanner.etc.FPSObject;

public abstract class AbstractPersistable extends FPSObject implements IPersistable {

    /** serialVersionUID */
    private static final long serialVersionUID = -6401068629850638057L;

    protected transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        AbstractPersistable.sqlHelper = sqlHelper;
    }

    protected transient static ObjectDao objectDao;
    public static void setObjectDao(ObjectDao objectDao) {
        AbstractPersistable.objectDao = objectDao;
    }

    protected transient static LinkObjectDao linkObjectDao;
    public static void setLinkObjectDao(LinkObjectDao linkObjectDao) {
        AbstractPersistable.linkObjectDao = linkObjectDao;
    }

    protected transient static PersonBeanDao personDao;
    public static void setPersonDao(PersonBeanDao personDao) {
        AbstractPersistable.personDao = personDao;
    }

    protected transient static FinancialService financialService;
    public static void setFinancialService(FinancialService financialService) {
        AbstractPersistable.financialService = financialService;
    }

    protected AbstractPersistable() {
        super();
    }

    protected AbstractPersistable(Integer ownerId) {
        super(ownerId);
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
    public Connection getConnection() throws SQLException {
        return sqlHelper.getConnection();
    }

    public void close(ResultSet rs, Statement sql) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (sql != null) {
            sql.close();
        }
    }

    /**
     * 
     */
    public int getIdentityID(Connection con) throws SQLException {
        return objectDao.getIdentityID(con);
    }

    public int getNewObjectID(int objectTypeID, Connection con) throws SQLException {
        return objectDao.getNewObjectID(objectTypeID, con);
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
    public int setLink(int linkObjectTypeID, Connection con) throws SQLException {
        // first level link
        return linkObjectDao.link(getOwnerId(), getId(), linkObjectTypeID, con);

    }

    // second level link
    public int setLink(int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws SQLException {

        int linkID = setLink(linkObjectTypeID1, con);

        linkID = linkObjectDao.link(linkID, objectID2,
                linkObjectTypeID2, con);

        return linkID;

    }

    public List getLinkedObjects(int linkObjectTypeID, Connection con)
            throws SQLException {
        if (getId() == null)
            return null;
        return linkObjectDao.getLinkedObjects(
                getId().intValue(), linkObjectTypeID, con);

    }

    protected List getLinkedObjects(int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws SQLException {
        if (getId() == null)
            return null;
        return linkObjectDao.getLinkedObjects(
                getId().intValue(), linkObjectTypeID1, objectID2,
                linkObjectTypeID2, con);

    }

    /**
     * HELPER METHODS
     */
    // Display an SQLException which has occured in this application.
    protected static void showSQLException(SQLException e) {
        // Notice that a SQLException is actually a chain of SQLExceptions,
        // let's not forget to print all of them...
        SQLException next = e;
        while (next != null) {
            System.err.println(next.getMessage());
            System.err.println("Error Code: " + next.getErrorCode());
            System.err.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }

}
