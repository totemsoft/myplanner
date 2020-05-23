/*
 * FPSLinkObject.java
 *
 * Created on 24 August 2001, 10:20
 */

package au.com.totemsoft.myplanner.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.dao.LinkObjectDao;

public class LinkObjectDaoImpl implements LinkObjectDao {

    @Autowired private SQLHelper sqlHelper;

    public int getLinkID(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        Integer objID2 = objectID2 >= 0 ? new Integer(objectID2) : null;

        return getLinkID(new Integer(objectID1), objID2, linkObjectTypeID, con);
    }

    public int getLinkID(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        if ((objectID1 == null || objectID1.intValue() < 0)
                || (objectID2 != null && objectID2.intValue() < 0)
                || linkObjectTypeID < 0 || con == null)
            throw new SQLException("Invalid getLinkID( " + objectID1 + ", "
                    + objectID2 + ", " + linkObjectTypeID + " ) parameter.");

        PreparedStatement sql = null;
        ResultSet rs = null;

        // check if this link already exists
        try {
            sql = con
                    .prepareStatement("SELECT LinkID FROM Link"
                            + " WHERE"
                            + " (ObjectID1=?)"
                            + (objectID2 == null ? " AND ( ObjectID2 IS NULL )"
                                    : objectID2.intValue() > 0 ? " AND ( ObjectID2 = ? )"
                                            : "") + " AND (LinkObjectTypeID=?)"
                            + " AND (LogicallyDeleted IS NULL)");

            int i = 0;
            sql.setInt(++i, objectID1.intValue());
            if (objectID2 != null && objectID2.intValue() > 0)
                sql.setInt(++i, objectID2.intValue());
            sql.setInt(++i, linkObjectTypeID);

            rs = sql.executeQuery();

            if (rs.next())
                return rs.getInt(1);
            return LINK_NOT_FOUND;
        } finally {
            if (rs != null)
                rs.close();
            if (sql != null)
                sql.close();
        }

    }

    public int link(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        Integer objID2 = objectID2 == 0 ? null : new Integer(objectID2);

        return link(new Integer(objectID1), objID2, linkObjectTypeID, true, con);
    }

    public int link(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        return link(objectID1, objectID2, linkObjectTypeID, true, con);
    }

    public int link(int objectID1, int objectID2,
            int linkObjectTypeID, boolean check, Connection con)
            throws SQLException {

        Integer objID2 = objectID2 == 0 ? null : new Integer(objectID2);

        return link(new Integer(objectID1), objID2, linkObjectTypeID, check,
                con);
    }

    public int link(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, boolean check, Connection con)
            throws SQLException {

        int linkID;

        if (check) {
            linkID = getLinkID(objectID1, objectID2, linkObjectTypeID, con);
            if (linkID != LINK_NOT_FOUND)
                return linkID;
        }

        PreparedStatement sql = null;

        try {
            // does not exists, lets create one
            linkID = sqlHelper.getNewObjectID(linkObjectTypeID, con);

            sql = con
                    .prepareStatement("INSERT INTO Link ( LinkID, ObjectID1, ObjectID2, LinkObjectTypeID )"
                            + " VALUES (?, ?, ?, ?)");
            sql.setInt(1, linkID);
            sql.setObject(2, objectID1, java.sql.Types.INTEGER);
            sql.setObject(3, objectID2, java.sql.Types.INTEGER);
            sql.setInt(4, linkObjectTypeID);
            sql.executeUpdate();

            return linkID;

        } finally {
            if (sql != null)
                sql.close();
        }

    }

    public int unlink(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        return unlink(new Integer(objectID1), new Integer(objectID2),
                linkObjectTypeID, con);
    }

    public int unlink(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException {

        int linkID = getLinkID(objectID1, objectID2, linkObjectTypeID, con);
        if (linkID == LINK_NOT_FOUND)
            return LINK_NOT_FOUND;

        PreparedStatement sql = null;

        try {
            // this link can be linked to some other link/object !!!
            sql = con.prepareStatement(
            // "DELETE FROM Link WHERE ( LinkID=? )"
                    "UPDATE Link SET LogicallyDeleted='Y' WHERE ( LinkID=? )");
            sql.setInt(1, linkID);
            sql.executeUpdate();

            return linkID;

        } finally {
            if (sql != null)
                sql.close();
        }

    }

    // use LinkID
    public int updateLinkForObject2(Integer linkID,
            Integer newObjectID2,
            // int linkObjectTypeID,
            Connection con) throws SQLException {

        PreparedStatement sql = null;
        try {
            sql = con
                    .prepareStatement("UPDATE Link SET ObjectID2=? WHERE (LinkID=?)");
            sql.setObject(1, newObjectID2, java.sql.Types.INTEGER);
            sql.setObject(2, linkID, java.sql.Types.INTEGER);
            return sql.executeUpdate();

        } finally {
            if (sql != null)
                sql.close();
        }

    }

    // use ObjectID1
    public int updateLinksForObject2(Integer objectID1,
            Integer newObjectID2, int linkObjectTypeID, Connection con)
            throws SQLException {

        PreparedStatement sql = null;
        try {
            sql = con.prepareStatement("SELECT LinkID FROM Link"
                    + " WHERE (ObjectID1=?) AND (LinkObjectTypeID=?)");
            sql.setInt(1, objectID1.intValue());
            sql.setInt(2, linkObjectTypeID);

            int linkID = LINK_NOT_FOUND;
            ResultSet rs = null;
            try {
                rs = sql.executeQuery();
                if (!rs.next())
                    return LINK_NOT_FOUND;

                linkID = rs.getInt(1);

                if (rs.next())
                    throw new SQLException("Oops.. more than one link record");
            } finally {
                if (rs != null)
                    rs.close();
                if (sql != null) {
                    sql.close();
                    sql = null;
                }
            }

            sql = con
                    .prepareStatement("UPDATE Link SET ObjectID2=? WHERE (LinkID=?)");
            sql.setObject(1, newObjectID2, java.sql.Types.INTEGER);
            sql.setInt(2, linkID);
            sql.executeUpdate();

            return linkID;

        } finally {
            if (sql != null)
                sql.close();
        }

    }

    /**
     * 
     */
    public List getLinkedObjects(int ownerPrimaryKeyID,
            int linkObjectTypeID, Connection con) throws SQLException {

        return getLinkedObjects(ownerPrimaryKeyID, linkObjectTypeID, true, con);

    }

    public List getLinkedObjects(int ownerPrimaryKeyID,
            int linkObjectTypeID, // first level linkage
            boolean toObjectID1, Connection con) throws SQLException {

        List list = null;

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con.prepareStatement(toObjectID1 ? "SELECT ObjectID2"
                    + " FROM Link"
                    + " WHERE ( ObjectID1=? ) AND ( LinkObjectTypeID=? )"
                    + " AND ( LogicallyDeleted IS NULL )" : "SELECT ObjectID1"
                    + " FROM Link"
                    + " WHERE ( ObjectID2=? ) AND ( LinkObjectTypeID=? )"
                    + " AND ( LogicallyDeleted IS NULL )",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, ownerPrimaryKeyID);
            sql.setInt(2, linkObjectTypeID);
            rs = sql.executeQuery();

            while (rs.next()) {
                if (list == null)
                    list = new ArrayList();
                list.add(rs.getObject(toObjectID1 ? "ObjectID2" : "ObjectID1"));
            }

            return list;

        } finally {
            sqlHelper.close(rs, sql);
        }

    }

    public List getLinkedObjects(
            int ownerPrimaryKeyID, int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws SQLException {

        return getLinkedObjects(ownerPrimaryKeyID, linkObjectTypeID1,
                objectID2, linkObjectTypeID2, true, con);

    }

    public List getLinkedObjects(
            int ownerPrimaryKeyID, int linkObjectTypeID1, // first level
                                                            // linkage
            int objectID2, int linkObjectTypeID2, boolean toObjectID1, // second
                                                                        // level
                                                                        // linkage
            Connection con) throws SQLException {

        ArrayList list = null;

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con
                    .prepareStatement(
                            toObjectID1 ? "SELECT ObjectID2"
                                    + " FROM Link"
                                    + " WHERE ( ObjectID1 = ? ) AND ( LinkObjectTypeID = ? )"
                                    + " AND ( LinkID IN"
                                    + " (SELECT ObjectID1"
                                    + " FROM Link"
                                    + " WHERE"
                                    + (objectID2 <= 0 ? " ( ObjectID2 IS NULL )"
                                            : " ( ObjectID2 = ? )")
                                    + " AND ( LinkObjectTypeID = ? )"
                                    + " AND ( LogicallyDeleted IS NULL )"
                                    + " )"
                                    + " AND ( LogicallyDeleted IS NULL )"
                                    + " )"
                                    : "SELECT ObjectID1"
                                            + " FROM Link"
                                            + " WHERE ( ObjectID2 = ? ) AND ( LinkObjectTypeID = ? )"
                                            + " AND ( LinkID IN"
                                            + " (SELECT ObjectID2"
                                            + " FROM Link"
                                            + " WHERE"
                                            + (objectID2 <= 0 ? " ( ObjectID1 IS NULL )"
                                                    : " ( ObjectID1 = ? )")
                                            + " AND ( LinkObjectTypeID = ? )"
                                            + " AND ( LogicallyDeleted IS NULL )"
                                            + " )"
                                            + " AND ( LogicallyDeleted IS NULL )"
                                            + " )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            int i = 0;
            sql.setInt(++i, ownerPrimaryKeyID);
            sql.setInt(++i, linkObjectTypeID1);
            if (objectID2 > 0)
                sql.setInt(++i, objectID2);
            sql.setInt(++i, linkObjectTypeID2);
            rs = sql.executeQuery();

            while (rs.next()) {
                if (list == null)
                    list = new ArrayList();
                list.add(rs.getObject(toObjectID1 ? "ObjectID2" : "ObjectID1"));
            }
            return list;

        } finally {
            sqlHelper.close(rs, sql);
        }

    }

}
