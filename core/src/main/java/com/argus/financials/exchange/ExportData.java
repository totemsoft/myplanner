/*
 * ExportData.java
 *
 * Created on 3 June 2003, 11:55
 */

package com.argus.financials.exchange;

import com.argus.dao.SQLHelper;
import com.argus.io.IOUtils2;

public class ExportData {

    private static final String XML_HEADER = "<?xml version=\"" + "1.0"
            + "\" encoding=\"" + IOUtils2.ENCODING + "\" ?>";

    private static final String DATA_START = "<data>";

    private static final String DATA_END = "</data>";

    private static final String DELIM = ", RecordDelimiter ";

    private static String DEFAULT_XSL = "com/argus/financials/exchange/XMLtoSQLsp.xsl";

    private transient static SQLHelper sqlHelper;
    public static void setSqlHelper(SQLHelper sqlHelper) {
        ExportData.sqlHelper = sqlHelper;
    }

    private StringBuffer sb;

    private Integer userPersonID;

    private Integer clientPersonID;

    /** Creates a new instance of ExportData */
    public ExportData() {
        this(null, null);
    }

    public ExportData(Integer userPersonID) {
        this(userPersonID, null);
    }

    public ExportData(int userPersonID, int clientPersonID) {
        this(new Integer(userPersonID), new Integer(clientPersonID));
    }

    public ExportData(Integer userPersonID, Integer clientPersonID) {
        sb = new StringBuffer();

        this.userPersonID = userPersonID;
        this.clientPersonID = clientPersonID;

    }

    public String toString() {
        return sb.toString();
    }

    public Integer getUserPersonID() {
        return this.userPersonID;
    }

    public void setUserPersonID(Integer userPersonID) {
        this.userPersonID = userPersonID;
    }

    public Integer getClientPersonID() {
        return this.clientPersonID;
    }

    public void setClientPersonID(Integer clientPersonID) {
        this.clientPersonID = clientPersonID;
    }

    public StringBuffer getExportData() {
        return sb;
    }

    public void open(String fileName) throws java.io.IOException {

        clear();
        java.io.FileInputStream fis = new java.io.FileInputStream(fileName);
        try {
            int b;
            while ((b = fis.read()) != -1)
                sb.append((char) b);

        } finally {
            fis.close();
        }

    }

    public void save(String fileName) throws java.io.IOException {

        java.io.FileOutputStream fos = new java.io.FileOutputStream(fileName);
        java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos);
        try {
            bos.write(getExportData().toString().getBytes());
        } finally {
            bos.close();
            fos.close();
        }

    }

    public void clear() {
        sb.setLength(0);
    }

    public void execute() throws java.sql.SQLException, java.io.IOException {

        clear();

        // //////////////////////////////////////////////////////////////////////
        sb.append(XML_HEADER);
        sb.append('\n');
        sb.append(DATA_START);
        sb.append('\n');
        // //////////////////////////////////////////////////////////////////////

        String sql;
        java.sql.Connection con = sqlHelper.getConnection();
        try {
            // //////////////////////////////////////////////////////////////////
            sql = "EXEC sp_export_initialize " + userPersonID;
            execute(con, sql);

            // //////////////////////////////////////////////////////////////////
            // reference data
            // //////////////////////////////////////////////////////////////////
            sql = "EXEC sp_export_References";
            execute(con, sql);

            // //////////////////////////////////////////////////////////////////
            // user personal data
            // //////////////////////////////////////////////////////////////////
            sql = "EXEC sp_export_UserPerson " + userPersonID;
            execute(con, sql);

            // //////////////////////////////////////////////////////////////////
            // client personal data
            // //////////////////////////////////////////////////////////////////
            sql = "EXEC sp_export_ClientPerson " + clientPersonID + ", "
                    + userPersonID;
            execute(con, sql);

            // //////////////////////////////////////////////////////////////////
            sql = "EXEC sp_export_uninitialize " + userPersonID;
            execute(con, sql);

            // //////////////////////////////////////////////////////////////////
            sb.append(DATA_END);
            // //////////////////////////////////////////////////////////////////

        } catch (java.sql.SQLException e) {
            clear();
            throw e;

        } catch (java.io.IOException e) {
            clear();
            throw e;
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private void execute(java.sql.Connection con, String sql)
            throws java.sql.SQLException, java.io.IOException {
        java.sql.ResultSet rs = null;
        java.sql.Statement stmt = con.createStatement();
        try {
            if (!stmt.execute(sql))
                return;

            String s = null;
            while (rs == null || stmt.getMoreResults()) {
                rs = stmt.getResultSet();

                while (rs.next()) {
                    java.sql.Clob clob = rs.getClob(1);

                    s = clob.getSubString(1L, (int) clob.length());

                    if (s.length() > 0)
                        sb.append(s);
                }

                if (s != null)
                    sb.append('\n');

            }

        } finally {
            if (rs != null)
                rs.close();
            stmt.close();
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void execute(java.util.Collection c) throws java.sql.SQLException {
        execute((String[]) c.toArray(new String[0]));
    }

    public void execute(String[] sql) throws java.sql.SQLException {

        if (sql == null || sql.length == 0)
            return;

        java.sql.Connection con = sqlHelper.getConnection();
        try {

            for (int i = 0; i < sql.length; i++) {
                java.sql.CallableStatement cs = con.prepareCall(sql[i]);
                try {
                    cs.execute();
                } finally {
                    cs.close();
                }

            }
            /*
             * java.sql.Statement stmt = con.createStatement(); try { for ( int
             * i = 0; i < sql.length; i++ ) { stmt.addBatch( sql[i] );
             * 
             * int [] results = stmt.executeBatch(); for ( int i = 0; i <
             * results.length; i++ ) //if ( results[i] < 0 ) System.out.println( "" +
             * i + ").\t Result=" + results[i] + ",\t" + sql[i] );
             *  } finally { stmt.close(); }
             */
            con.commit();

        } catch (java.sql.SQLException e) {
            con.rollback();
            clear();

            throw e;
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private String replace(String source, String replace, String replaceWith) {

        if (replace == null || replaceWith == null)
            return source;

        int len = replace.length();

        int indexPrev = 0;
        int indexNext = source.indexOf(replace);
        while (indexNext >= indexPrev) {
            source = source.substring(0, indexNext) + replaceWith
                    + source.substring(indexNext + len);
            indexPrev = indexNext + len;
            indexNext = source.indexOf(replace);
        }

        return source;

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public java.util.Collection transformXML2SQL() throws java.io.IOException {
        return transformXML2SQL(null);
    }

    public java.util.Collection transformXML2SQL(String xsl)
            throws java.io.IOException {

        String sql;
        if (xsl == null) {
            xsl = DEFAULT_XSL;

            ClassLoader loader = getClass().getClassLoader();

            // null if the resource could not be found or the caller doesn't
            // have adequate privileges to get the resource.
            java.net.URL url = loader.getResource(xsl);

            if (url == null)
                throw new java.io.IOException("URL can not be found: " + xsl);

            java.io.InputStream is = url.openStream();
            try {
                sql = com.argus.financials.exchange.XMLTransformService
                        .transform(getExportData().toString(), is);
            } finally {
                is.close();
            }

        } else {

            java.io.File file = new java.io.File(xsl);
            if (!file.exists())
                throw new java.io.IOException("File can not be found: " + xsl);

            xsl = file.getCanonicalPath();

            sql = com.argus.financials.exchange.XMLTransformService.transform(
                    getExportData().toString(), xsl);

        }

        java.util.Vector c = new java.util.Vector();

        int len = DELIM.length();
        int indexNext = sql.indexOf(DELIM);
        while (indexNext > 0) {
            c.add(sql.substring(0, indexNext));

            sql = sql.substring(indexNext + len);
            indexNext = sql.indexOf(DELIM);
        }

        return c;

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public void exportFile(Integer userPersonID, Integer clientPersonID,
            java.io.File file) throws java.sql.SQLException,
            java.io.IOException {
        exportFile(userPersonID, clientPersonID, file.getCanonicalPath());
    }

    public void exportFile(Integer userPersonID, Integer clientPersonID,
            String file) throws java.sql.SQLException, java.io.IOException {
        setUserPersonID(userPersonID);
        setClientPersonID(clientPersonID);

        clear();
        execute();
        save(file);
    }

    public void importFile(java.io.File file) throws java.sql.SQLException,
            java.io.IOException {
        importFile(file.getCanonicalPath());
    }

    public void importFile(String file) throws java.sql.SQLException,
            java.io.IOException {
        clear();
        open(file);
        execute(transformXML2SQL());
    }

}
