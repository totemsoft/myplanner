/*
 * DummyMetaData.java
 *
 * Created on 16 April 2003, 11:44
 */

package com.argus.financials.swing.table;

import java.sql.SQLException;

/**
 * 
 * @author shibaevv
 * @version
 */
public class DummyMetaData implements java.sql.ResultSetMetaData {

    private String[] columns;

    /** Creates new DummyMetaData */
    public DummyMetaData(String[] columns) {
        this.columns = columns;
    }

    public java.lang.String getColumnTypeName(int param)
            throws java.sql.SQLException {
        return "String";
    }

    public java.lang.String getColumnClassName(int param)
            throws java.sql.SQLException {
        return String.class.getName();
    }

    public int getScale(int param) throws java.sql.SQLException {
        return 0;
    }

    public java.lang.String getColumnLabel(int param)
            throws java.sql.SQLException {
        return columns[param - 1];
    }

    public boolean isAutoIncrement(int param) throws java.sql.SQLException {
        return false;
    }

    public int getColumnDisplaySize(int param) throws java.sql.SQLException {
        return 10;
    }

    public java.lang.String getCatalogName(int param)
            throws java.sql.SQLException {
        return "Unknown";
    }

    public java.lang.String getColumnName(int param)
            throws java.sql.SQLException {
        return columns[param - 1];
    }

    public boolean isWritable(int param) throws java.sql.SQLException {
        return true;
    }

    public boolean isSearchable(int param) throws java.sql.SQLException {
        return true;
    }

    public int getColumnType(int param) throws java.sql.SQLException {
        return java.sql.Types.VARCHAR;
    }

    public boolean isCurrency(int param) throws java.sql.SQLException {
        return false;
    }

    public java.lang.String getTableName(int param)
            throws java.sql.SQLException {
        return "Unknown";
    }

    public int isNullable(int param) throws java.sql.SQLException {
        return java.sql.ResultSetMetaData.columnNullable;
    }

    public boolean isSigned(int param) throws java.sql.SQLException {
        return false;
    }

    public boolean isReadOnly(int param) throws java.sql.SQLException {
        return false;
    }

    public boolean isDefinitelyWritable(int param) throws java.sql.SQLException {
        return false;
    }

    public int getPrecision(int param) throws java.sql.SQLException {
        return 0;
    }

    public java.lang.String getSchemaName(int param)
            throws java.sql.SQLException {
        return "Uknown";
    }

    public boolean isCaseSensitive(int param) throws java.sql.SQLException {
        return false;
    }

    public int getColumnCount() throws java.sql.SQLException {
        return columns.length;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
