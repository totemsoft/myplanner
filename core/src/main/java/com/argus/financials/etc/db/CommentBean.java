/*
 * CommentBean.java
 *
 * Created on 14 November 2001, 16:36
 */

package com.argus.financials.etc.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.etc.Comment;

public class CommentBean extends AbstractPersistable {

    private Comment comment; // aggregation

    // First level of linkage (e.g. objectTypeID1 = PERSON or BUSINESS),
    // objectTypeID2 = COMMENT
    // Second level of linkage will be
    // objectTypeID1 = getLinkObjectTypeID(), objectTypeID2 (e.g.
    // FINANCIAL_GOAL)
    // ( but we will supply linkObjectTypeID (e.g.
    // PERSON$COMMENT_2_FINANCIALGOAL) )
    private int[] linkObjectTypes = new int[2];

    /**
     * Creates new CommentBean
     */
    public CommentBean() {
    }

    public CommentBean(Comment value) {
        this.comment = value;
    }

    /**
     * helper methods
     */
    public Class getCommentClass() {
        return Comment.class;
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.COMMENT;
    }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        case 2:
            return linkObjectTypes[1];
        default:
            throw new RuntimeException(
                    "CommentBean.getLinkObjectTypeID() Invalid linkage level: "
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
        case 2: {
            setModified(linkObjectTypes[1] != value);
            linkObjectTypes[1] = value;
            break;
        }
        default:
            throw new RuntimeException(
                    "CommentBean.setLinkObjectTypeID() Invalid linkage level: "
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

        boolean newConnection = (con == null);
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            if (newConnection)
                con = this.getConnection();

            sql = con.prepareStatement("SELECT CommentText" + " FROM Comment"
                    + " WHERE (CommentID = ?)", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Comment ID: "
                        + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setId(primaryKeyID);

        } finally {
            close(rs, sql);
            if (newConnection && con != null)
                con.close();
        }

    }

    public void load(ResultSet rs) throws SQLException {

        // Comment table
        setCommentText(rs.getString("CommentText"));

    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = 0;

        int i = 0;
        PreparedStatement sql = null;

        try {
            if (getId() == null || getId().intValue() < 0) {

                // CommentText field IS NOT NULL
                if (getCommentText() == null)
                    return 0;

                primaryKeyID = getNewObjectID(getObjectTypeID(), con);

                // do insert into Comment table
                sql = con.prepareStatement("INSERT INTO Comment"
                        + " (CommentID, CommentText)" + " VALUES" + " (?,?)");

                sql.setInt(++i, primaryKeyID);
                sql.setString(++i, getCommentText());

                sql.executeUpdate();

                setId(new Integer(primaryKeyID));

                int linkID = setLink(getLinkObjectTypeID(1), 0,
                        getLinkObjectTypeID(2), // 0 here == null
                        con);

            } else {

                // CommentText field IS NOT NULL
                if (getCommentText() == null) {
                    // this.remove( con );
                    // return 0;
                    setCommentText("Put your commont here ...");
                }

                primaryKeyID = getId().intValue();

                // do update on Comment table
                sql = con.prepareStatement("UPDATE Comment SET"
                        + " CommentText=?" + " WHERE CommentID=?");

                sql.setString(++i, getCommentText());

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
    public Comment getComment() {
        if (comment == null)
            comment = new Comment();

        return comment;
    }

    public void setComment(Comment value) {

        // boolean modified = ;

        comment = value;
        if (comment != null)
            setModified(true);
    }

    public boolean isModified() {
        return getComment().isModified();
    }

    public void setModified(boolean value) {
        getComment().setModified(value);
    }

    public Integer getId() {
        return getComment().getId();
    }

    public void setId(Integer value) {
        getComment().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getComment().getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getComment().setOwnerId(value);
    }

    public String getCommentText() {
        return getComment().getCommentText();
    }

    public void setCommentText(String value) {
        getComment().setCommentText(value);
    }

}
