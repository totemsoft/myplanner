/*
 * DataConverter.java
 *
 * Created on 8 January 2003, 16:03
 */

package com.argus.financials.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.util.KeyValue;

/**
 * 
 * @version
 */
public class DataConverter extends AbstractPersistable {

    private static DataConverter dc = null;

    /** Creates new DataConverter */
    private DataConverter() {
    }

    public static DataConverter getInstance() {
        if (dc == null)
            dc = new DataConverter();
        return dc;
    }

    public static DefaultTableModel getResultSetAsTable(String query) {

        DefaultTableModel tm = new DefaultTableModel();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getInstance().getConnection();

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();

            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector(numberOfColumns);

            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.add(column, metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();
            while (rs.next()) {
                Vector newRow = new Vector();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    newRow.addElement(rs.getObject(i));
                }
                rows.addElement(newRow);
            }

            rs.close();
            stmt.close();

            tm = new DefaultTableModel(rows, columnNames) {

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

            };

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tm;
    }

    public static DefaultTableModel getEmptyModel(String title, String content) {

        Object[][] data = new Object[1][1];
        data[0][0] = content;
        // data.addElement(content);

        Object[] columnNames = new Object[1];
        columnNames[0] = title;

        DefaultTableModel tm = new DefaultTableModel(data, columnNames);

        return tm;
    }

    public static UpdateableTableModel getResultSetAsUpdateableTable(
            String query) {

        UpdateableTableModel tm = null;

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getInstance().getConnection();

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);

            tm = new UpdateableTableModel(rs);

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tm;
    }

    public static UpdateableTableModel getResultSetAsUpdateableTable(
            DataProvider dp) {

        UpdateableTableModel tm = null;

        if (dp == null)
            return tm;

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getInstance().getConnection();
            stmt = con.createStatement();

            String query = dp.getSelect();

            if (query == null)
                return tm;

            rs = stmt.executeQuery(query);

            tm = new UpdateableTableModel(rs);

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tm;
    }

    public static int commitChangesToDatabase(DataResolver dr) {

        int status = -1;

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        HashMap idValues = new HashMap();
        try {
            con = getInstance().getConnection();

            stmt = con.createStatement();

            ArrayList inserts = dr.getInserts();
            ArrayList deletes = dr.getDeletes();
            ArrayList updates = dr.getUpdates();
            ArrayList queries = new ArrayList();

            if (inserts != null)
                queries.addAll(inserts);
            if (deletes != null)
                queries.addAll(deletes);
            if (updates != null)
                queries.addAll(updates);

            if (queries != null) {
                for (int i = 0; i < queries.size(); i++) {
                    KeyValue kv = (KeyValue) queries.get(i);
                    String sql = (String) kv.getValue();
                    String command = (String) kv.getKey();
                    Object[] idNames = kv.getSupplement();

                    if (command.equals(dr.PLAIN)) {
                        stmt.execute(sql);
                    } else if (command.equals(dr.GET_IDENTITY)
                            && idNames != null && idNames.length > 0) {
                        rs = stmt.executeQuery(sql);
                        if (rs != null && rs.next()) {
                            Object value = rs.getObject(1);

                            idValues.put(idNames[0], value);
                        }

                    } else if (command.equals(dr.SET_IDENTITY)
                            && idNames != null && idNames.length > 0) {
                        pstmt = con.prepareStatement(sql);
                        for (int j = 0; j < idNames.length; j++) {
                            Object value = idValues.get(idNames[j]);

                            if (value != null)
                                pstmt.setObject(j + 1, value);
                        }
                        pstmt.execute();

                    } else if (command.equals(dr.COMMIT)) {
                        idValues.clear();
                    }
                }
            }

            dr.getDeletes();
            dr.getUpdates();

            stmt.close();
            status = 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return status;
    }

    /**
     * Verification routine . Returns true if result set exists , false -
     * otherwise
     * 
     * 
     * 
     */

    public static boolean verify(DataVerifier dr, int requestID,
            Object[] criteria) {

        boolean status = false;

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        HashMap idValues = new HashMap();
        try {
            con = getInstance().getConnection();
            stmt = con.createStatement();

            String sql = dr.getVerificationRequest(requestID, criteria);

            rs = stmt.executeQuery(sql);
            if (rs == null || !rs.first())
                status = false;
            else
                status = true;

            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return status;
    }

    /** We don't need all this stuff but have to have it */
    public void remove(Connection con) throws SQLException {
    }

    public int store(Connection con) throws SQLException {
        return -1;
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {
    }

    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
    }

    public void load(ResultSet rs) throws SQLException {
    }

}
