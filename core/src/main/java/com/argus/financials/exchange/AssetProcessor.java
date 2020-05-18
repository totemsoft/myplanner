/*
 * AssetProcessor.java
 *
 * Created on 30 June 2003, 09:33
 */

package com.argus.financials.exchange;

import com.argus.dao.SQLHelper;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import com.argus.financials.assetinvestment.AvailableInvestmentsTableRow;

public class AssetProcessor {

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        AssetProcessor.sqlHelper = sqlHelper;
    }

    private FinancialCode financialCode;

    private FinancialCode financialCode_New;

    /** Creates a new instance of AssetProcessor */
    public AssetProcessor() {
    }

    public void clear() {
        financialCode = null;
        financialCode_New = null;
    }

    public FinancialCode getFinancialCode() {
        if (financialCode == null)
            financialCode = new FinancialCode();
        return financialCode;
    }

    public void setFinancialCode(FinancialCode value) {
        financialCode = value;
    }

    public FinancialCode getFinancialCodeNew() {
        if (financialCode_New == null)
            financialCode_New = new FinancialCode();
        return financialCode_New;
    }

    public void setFinancialCodeNew(FinancialCode value) {
        financialCode_New = value;
    }

    public java.util.Vector getJournals() throws java.sql.SQLException {

        java.sql.Connection con = sqlHelper.getConnection();

        java.sql.ResultSet rs = null;
        java.sql.PreparedStatement stmt = con
                .prepareStatement("EXEC sp_select_Journal");

        java.util.Vector rows = new java.util.Vector();

        try {
            // //////////////////////////////////////////////////////////////////
            // EXEC sp_select_Journal
            // @JournalStatusID int = 1 -- 1 = 'Assets Swaped'
            // //////////////////////////////////////////////////////////////////

            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            if (!stmt.execute())
                return rows;

            while (rs == null || stmt.getMoreResults()) {
                rs = stmt.getResultSet();

                while (rs.next()) {

                    java.util.Vector row = new java.util.Vector();

                    // SELECTED
                    row.add(Boolean.FALSE);

                    // BATCH
                    Integer batch = (Integer) rs.getObject("JournalID");
                    row.add(batch);

                    // JOURNAL
                    String desc = rs.getString("JournalDesc");
                    java.util.Date date = rs.getDate("DateCreated");
                    java.sql.Time time = rs.getTime("TimeCreated");

                    row.add(new Journal(batch.intValue(), desc, date));

                    // DATE
                    row.add(date);

                    // TIME
                    row.add(time);

                    rows.add(row);

                }

            }

        } finally {
            if (rs != null)
                rs.close();
            stmt.close();

        }

        return rows;

    }

    public java.util.Vector getFinancialRows() throws java.sql.SQLException {

        java.sql.Connection con = sqlHelper.getConnection();

        java.sql.ResultSet rs = null;
        java.sql.PreparedStatement stmt = con
                .prepareStatement("EXEC sp_select_Financial ?");

        java.util.Vector rows = new java.util.Vector();

        try {
            // //////////////////////////////////////////////////////////////////
            // EXEC sp_select_Financial
            // @FinancialCodeID int,
            // @FinancialTypeID int = NULL
            // //////////////////////////////////////////////////////////////////

            int i = 0;
            stmt.setInt(++i, getFinancialCode().cid);

            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            if (!stmt.execute())
                return rows;

            StringBuffer sb = new StringBuffer();
            while (rs == null || stmt.getMoreResults()) {
                rs = stmt.getResultSet();

                while (rs.next()) {
                    sb.setLength(0);

                    // FinancialID, -- FinancialDesc,
                    // Institution,
                    // UnitsShares, UnitsSharesPrice, PriceDate

                    String s = rs.getString("Institution");
                    sb.append(s == null ? "" : s.trim() + ", ");

                    java.math.BigDecimal d = rs.getBigDecimal("UnitsShares");
                    sb.append(d == null ? "" : "Units: " + d + ", ");

                    d = rs.getBigDecimal("UnitsSharesPrice");
                    sb.append(d == null ? "" : "Price: " + d + ", ");

                    java.util.Date date = rs.getDate("PriceDate");
                    sb.append(date == null ? "" : "Date: " + date + ", ");

                    s = rs.getString("PersonName");
                    sb.append(s == null ? "" : "Used by " + s.trim());

                    java.util.Vector row = new java.util.Vector();
                    row.add(sb.toString());
                    rows.add(row);

                }

            }

        } finally {
            if (rs != null)
                rs.close();
            stmt.close();

        }

        return rows;

    }

    public java.util.Vector getFinancialJournalRows(Journal batch)
            throws java.sql.SQLException {

        java.sql.Connection con = sqlHelper.getConnection();

        java.sql.ResultSet rs = null;
        java.sql.PreparedStatement stmt = con
                .prepareStatement("EXEC sp_select_FinancialJournal ?, ?");

        java.util.Vector rows = new java.util.Vector();

        try {
            // //////////////////////////////////////////////////////////////////
            // EXEC sp_select_FinancialJournal
            // @JournalID int,
            // @FinancialCodeID int = NULL,
            // @FinancialTypeID int = NULL
            // //////////////////////////////////////////////////////////////////

            int i = 0;

            stmt.setInt(++i, batch.id);

            stmt.setInt(++i, getFinancialCode().cid); // !!! we select from
                                                        // FinancialJournal
                                                        // table (backup)

            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            if (!stmt.execute())
                return rows;

            StringBuffer sb = new StringBuffer();
            while (rs == null || stmt.getMoreResults()) {
                rs = stmt.getResultSet();

                while (rs.next()) {
                    sb.setLength(0);

                    // FinancialID, -- FinancialDesc,
                    // Institution,
                    // UnitsShares, UnitsSharesPrice, PriceDate

                    String s = rs.getString("Institution");
                    sb.append(s == null ? "" : s.trim() + ", ");

                    java.math.BigDecimal d = rs.getBigDecimal("UnitsShares");
                    sb.append(d == null ? "" : "Units: " + d + ", ");

                    d = rs.getBigDecimal("UnitsSharesPrice");
                    sb.append(d == null ? "" : "Price: " + d + ", ");

                    java.util.Date date = rs.getDate("PriceDate");
                    sb.append(date == null ? "" : "Date: " + date + ", ");

                    s = rs.getString("PersonName");
                    sb.append(s == null ? "" : "Used by " + s.trim());

                    java.util.Vector row = new java.util.Vector();
                    row.add(sb.toString());
                    rows.add(row);

                }

            }

        } finally {
            if (rs != null)
                rs.close();
            stmt.close();

        }

        return rows;

    }

    public Journal executeSwap(String journalDesc) throws java.sql.SQLException {

        // //////////////////////////////////////////////////////////////////
        // EXEC sp_update_FinancialCode
        // @FinancialTypeID_Old int = NULL,
        // @FinancialCodeID_Old int,
        // @FinancialTypeID_New int = NULL,
        // @FinancialCodeID_New int,
        // @JournalDesc varchar(255)
        // //////////////////////////////////////////////////////////////////
        Journal batch = null;

        java.sql.Connection con = sqlHelper.getConnection();

        // java.sql.Statement stmt = con.createStatement();
        java.sql.CallableStatement stmt = con
                .prepareCall("{?=call sp_update_FinancialCode(?,?,?,?,?) }");
        try {
            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            // boolean res = stmt.execute( sql );

            int i = 0;
            stmt.registerOutParameter(++i, java.sql.Types.INTEGER);

            // stmt.setNull( ++i, java.sql.Types.INTEGER ); // tid = NULL
            stmt.setInt(++i, getFinancialCode().tid);
            stmt.setInt(++i, getFinancialCode().cid);

            // stmt.setNull( ++i, java.sql.Types.INTEGER ); // tid = NULL
            stmt.setInt(++i, getFinancialCodeNew().tid);
            stmt.setInt(++i, getFinancialCodeNew().cid);

            stmt.setString(++i, journalDesc);

            String sql = "EXEC sp_update_FinancialCode "
                    + getFinancialCode().tid + ", " + getFinancialCode().cid
                    + ", " + getFinancialCodeNew().tid + ", "
                    + getFinancialCodeNew().cid + ", '" + journalDesc + "'";

            stmt.execute();

            batch = new Journal(stmt.getInt(1), journalDesc,
                    new java.util.Date());

            con.commit();

        } catch (java.sql.SQLException e) {
            con.rollback();
            throw e;

        } finally {
            stmt.close();
        }

        return batch;

    }

    public void executeRecover(Journal journal) throws java.sql.SQLException {

        java.sql.Connection con = sqlHelper.getConnection();
        java.sql.Statement stmt = con.createStatement();
        try {
            // //////////////////////////////////////////////////////////////////
            // EXEC sp_restore_FinancialCode
            // @JournalID int
            // //////////////////////////////////////////////////////////////////
            String sql = "EXEC sp_restore_FinancialCode " + "@JournalID="
                    + journal.id;
            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            boolean res = stmt.execute(sql);

            con.commit();

        } catch (java.sql.SQLException e) {
            con.rollback();
            throw e;

        } finally {
            stmt.close();
        }

    }

    public java.util.Vector executeDelete() throws java.sql.SQLException {

        java.util.Vector rows = new java.util.Vector();

        if (getFinancialCode().code == null)
            return rows;

        java.sql.Connection con = sqlHelper.getConnection();

        // //////////////////////////////////////////////////////////////////
        // EXEC sp_delete_FinancialCode
        // @FinancialCode varchar(32)
        // //////////////////////////////////////////////////////////////////
        java.sql.ResultSet rs = null;
        java.sql.PreparedStatement stmt = con
                .prepareStatement("EXEC sp_delete_FinancialCode ?");

        try {
            stmt.setString(1, getFinancialCode().code);

            // true if the next result is a ResultSet object;
            // false if it is an update count or there are no more results
            boolean res = stmt.execute();

            while (true) {

                if (res) {
                    // this is select
                    rs = stmt.getResultSet();
                    while (rs.next()) {
                        java.util.Vector row = new java.util.Vector();
                        row.add(rs.getString(1));
                        rows.add(row);
                    }

                } else if (stmt.getUpdateCount() != -1) {
                    // this is update

                } else {
                    // There are no more Results/UpdateCount
                    break;

                }

                // Moves to a Statement object's next result. It returns true if
                // this result is a ResultSet object.
                // This method also implicitly closes any current ResultSet
                // object obtained with the method getResultSet.
                res = stmt.getMoreResults();

            }

            con.commit();

            return rows;

        } catch (java.sql.SQLException e) {
            con.rollback();
            throw e;

        } finally {
            if (rs != null)
                rs.close();
            stmt.close();
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class Journal extends Object {

        private int id;

        private String desc;

        private java.util.Date date;

        Journal(int id, String desc, java.util.Date date) {
            this.id = id;
            this.desc = desc;
            this.date = date;
        }

        public String toString() {
            return "Batch "
                    + id
                    + (desc == null || desc.trim().length() == 0 ? "." : " - "
                            + desc);
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj == null || !(obj instanceof Journal))
                return false;

            return toString().equals(((Journal) obj).toString());
        }

    }

    public static class FinancialCode extends Object {

        private int cid;

        private int tid;

        private String code;

        private String desc;

        FinancialCode() {
        }

        public FinancialCode(AvailableInvestmentsTableRow row) {
            this(row.getFinancialCodeID(), row.getFinancialTypeID(),
                    row.investmentCode, row.description);
        }

        public FinancialCode(int code_id, int type_id, String code, String desc) {
            this.cid = code_id;
            this.tid = type_id;
            this.code = code;
            this.desc = desc;
        }

        public String toString() {
            return code + ", " + desc;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj == null || !(obj instanceof FinancialCode))
                return false;

            return toString().equals(((FinancialCode) obj).toString());
        }

    }

}
