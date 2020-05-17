package com.argus.financials.api.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface LinkObjectDao {

    int LINK_NOT_FOUND = -1;

    int getLinkID(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int getLinkID(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int link(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int link(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int link(int objectID1, int objectID2,
            int linkObjectTypeID, boolean check, Connection con)
            throws SQLException;

    int link(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, boolean check, Connection con)
            throws SQLException;

    int unlink(int objectID1, int objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int unlink(Integer objectID1, Integer objectID2,
            int linkObjectTypeID, Connection con) throws SQLException;

    int updateLinkForObject2(Integer linkID,
            Integer newObjectID2,
            // int linkObjectTypeID,
            Connection con) throws SQLException;

    int updateLinksForObject2(Integer objectID1,
            Integer newObjectID2, int linkObjectTypeID, Connection con)
            throws SQLException;

    List getLinkedObjects(int ownerPrimaryKeyID,
            int linkObjectTypeID, Connection con) throws SQLException;

    List getLinkedObjects(int ownerPrimaryKeyID,
            int linkObjectTypeID, // first level linkage
            boolean toObjectID1, Connection con) throws SQLException;

    List getLinkedObjects(
            int ownerPrimaryKeyID, int linkObjectTypeID1, int objectID2,
            int linkObjectTypeID2, Connection con) throws SQLException;

    List getLinkedObjects(
            int ownerPrimaryKeyID, int linkObjectTypeID1, // first level
                                                            // linkage
            int objectID2, int linkObjectTypeID2, boolean toObjectID1, // second
                                                                        // level
                                                                        // linkage
            Connection con) throws SQLException;

    
}