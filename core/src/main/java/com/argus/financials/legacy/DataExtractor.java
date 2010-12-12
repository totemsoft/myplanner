/*
 * parser.java
 *
 * Created on 25 May 2002, 21:08
 */

package com.argus.financials.legacy;

/**
 * 
 * @author shibaevv
 * @version
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.argus.financials.config.FPSLocale;

public class DataExtractor extends DefaultHandler {

    private boolean success;

    public static final String DATA_SYNC_REQUEST = "DATA_SYNC_REQUEST";

    private static boolean DEBUG = false;

    private boolean hasMore;

    // TODO: use ReferenceCode class to store table name/description
    //
    // public final static ReferenceCode [] MORNING_STAR = {
    // new ReferenceCode( 0, "apir-pic", "Asia Pacific Region Codes" );
    // new ReferenceCode( 1, "asset-allocation-data", "Asset Allocation Data" );
    // ...

    public final static String[] MORNING_STAR = { "apir-pic",
    /*
     * "asset-allocation-data", "asset-types", "asset-types-allocations",
     * "country-currency-names-codes",
     */
    "manager-data",
    /*
     * "manager-group", "performance-returns", "product-group-identifier-data",
     */
    "product-information",
    /*
     * "product-legal-type", "product-pool-net-assets",
     */
    "unit-price-data", "AssetAllocationData" };

    public final static String[] IRESS = { "iress-asset-name",
            "share-price-data" };

    private String tableName;

    private Connection con;

    private String webUser;

    private String webPassword;

    /** Creates new parser */
    public DataExtractor() {

    }

    private Connection getConnection() {
        return con;
    }

    private long getTimeStampID(String tableName) throws SQLException {

        Statement stmt = getConnection().createStatement();
        ResultSet rs = null;

        try {
            rs = stmt
                    .executeQuery(" select max( id ) from [" + tableName + "]");

            rs.next();

            return rs.getLong(1);

        } catch (SQLException e) {
            success = false;
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            stmt.close();

        }

    }

    private String getParameters() throws SQLException {

        long timeStampID = getTimeStampID(tableName); // select max(id) from
                                                        // ...

        return "&request=" + DATA_SYNC_REQUEST + "&user=" + webUser
                + "&password=" + webPassword + "&table=" + tableName
                + "&timeStampID=" + timeStampID;

    }

    public boolean downloadData(Connection con, String webUser,
            String webPassword) throws ExtractException, IOException {

        // int len = MORNING_STAR.length + IRESS.length;

        for (int i = 0; i < MORNING_STAR.length; i++) {
            if (MORNING_STAR[i] != null && MORNING_STAR[i].length() > 0)
                downloadData(con, MORNING_STAR[i], webUser, webPassword);

        }

        for (int i = 0; i < IRESS.length; i++) {
            if (IRESS[i] != null && IRESS[i].length() > 0)
                downloadData(con, IRESS[i], webUser, webPassword);

        }

        return hasMore;

    }

    // this section can throw IOException, means no web connection available
    protected HttpURLConnection getURLConnection(String params)
            throws IOException {

        try {
            URL url = new URL(com.argus.financials.config.FPSLocale
                    .getInstance().getDownloadServerURL()
                    + params);
            FPSLocale r = com.argus.financials.config.FPSLocale.getInstance();
            // URL url = new URL( params );
            DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();

            if (DEBUG)
                System.out.println(" url = " + url);
            return (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            success = false;
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw e;
        }

    }

    public boolean downloadData(Connection con, String tableName,
            String webUser, String webPassword) throws ExtractException,
            IOException {

        this.con = con;
        this.tableName = tableName;
        this.webUser = webUser;
        this.webPassword = webPassword;

        String params = "";
        try {
            params = getParameters();
        } catch (SQLException e) {
            success = false;
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw new ExtractException(e.getMessage());
        }

        HttpURLConnection f = getURLConnection(params);

        // set proxy
        java.util.Properties fooey = new java.util.Properties(System
                .getProperties());
        fooey.put("proxySet", "true");
        fooey.put("proxyHost", com.argus.financials.config.FPSLocale
                .getInstance().getProxyHost());
        fooey.put("proxyPort", com.argus.financials.config.FPSLocale
                .getInstance().getProxyPort());
        System.setProperties(fooey);

        f.setRequestMethod("POST");
        f.setDoOutput(true);
        f.setDoInput(true);

        if (DEBUG)
            System.out
                    .println(" =============== this is after HttpURLConnection f = getURLConnection( params )");
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(f.getInputStream())));
            if (DEBUG)
                System.out
                        .println(" =============== this is after BufferedReader ");
            File file = File.createTempFile(this.tableName, ".xml");
            if (DEBUG)
                System.out.println(" =============== this is after file ");
            PrintWriter out = new PrintWriter(new FileWriter(file));
            if (DEBUG)
                System.out
                        .println(" =============== this is after PrintWriter ");
            if (!DEBUG)
                file.deleteOnExit();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
            out.close();
            in.close();
            parseFile(file);
            file = null;
            inputLine = null;
            success = true;

        } catch (IOException e) {
            success = false;
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw e;
        } catch (ExtractException ee) {
            success = false;
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(ee));
            throw ee;
        } finally {
            return hasMore;
        }
    }

    private void parseFile(File file) throws ExtractException {

        try {
            XMLReader saxp = XMLReaderFactory
                    .createXMLReader("org.apache.xerces.parsers.SAXParser");
            saxp.setContentHandler(this);
            InputSource inputSource = new InputSource(new FileInputStream(file));
            inputSource.setEncoding("Cp1252");
            saxp.parse(inputSource);
            inputSource = null;
            saxp = null;

        } catch (Exception e) {
            success = false;
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw new ExtractException(e.getMessage());
        }

    }

    /***************************************************************************
     * org.xml.sax.helpers.DefaultHandler
     **************************************************************************/

    private java.util.Collection inserts;

    public void endDocument() throws SAXException {
        if (inserts == null || inserts.size() == 0)
            return;
        int[] results = null;
        try {
            Statement stmt = con.createStatement();
            java.util.Iterator iter = inserts.iterator();
            while (iter.hasNext()) {
                String sql = (String) iter.next();
                if (DEBUG)
                    System.out.println(sql);
                stmt.addBatch(sql);
            }
            results = stmt.executeBatch();
            stmt.clearBatch();
            con.commit();
        } catch (java.sql.BatchUpdateException e) {
            success = false;
            if (DEBUG) {
                int[] updateCounts = e.getUpdateCounts();
                for (int j = 0; j < updateCounts.length; j++) {
                    System.out.println(j + ". " + updateCounts[j]);
                }
            }
            try {
                con.rollback();
            } catch (SQLException e2) {
                throw new SAXException(e2);
            }
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw new SAXException(e);

        } catch (java.sql.SQLException e) {
            success = false;
            try {
                con.rollback();
            } catch (SQLException e2) {
                throw new SAXException(e);
            }
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw new SAXException(e);
        } catch (Exception e) {
            success = false;
            try {
                con.rollback();
            } catch (SQLException e2) {
                throw new SAXException(e2);
            }
            javax.swing.SwingUtilities
                    .invokeLater(new ReportExtractorStatus(e));
            throw new SAXException(e);
        }
        con = null;// remove this reference to connection
        inserts = null;
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        java.util.Properties properties = null;
        if (localName.equals("hasMoreElement")) {
            properties = getNameValues(attributes);
            java.util.Iterator iter = properties.values().iterator();
            while (iter.hasNext()) {
                String value = (String) iter.next();
                if (value != null && value.equalsIgnoreCase("true")) {
                    hasMore = true;
                } else
                    hasMore = false;
            }
        }

        if (localName.equals("record")) {

            properties = getNameValues(attributes); // name=value

            String names = "";
            String values = "";

            java.util.Iterator iter = properties.keySet().iterator();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                String value = properties.getProperty(name);
                if (value.indexOf("'") >= 0) {
                    java.util.StringTokenizer str = new java.util.StringTokenizer(
                            value, "'");
                    String token = "";
                    while (str.hasMoreTokens()) {
                        token += str.nextToken() + "''";
                    }
                    value = token.substring(0, token.lastIndexOf("''"));
                }
                names += "[" + name + "], ";
                values += "'" + value + "'" + ", ";// "?, ";
            }
            names = names.substring(0, names.lastIndexOf(','));
            values = values.substring(0, values.lastIndexOf(','));

            String sql = "insert into [" + tableName + "] (" + names + ")"
                    + " VALUES " + "(" + values + ")";
            // if ( DEBUG )System.out.println(sql);

            if (inserts == null)
                inserts = new java.util.Vector();
            inserts.add(sql);

        }
    }

    private java.util.Properties getNameValues(Attributes attributes) {

        java.util.Properties p = new java.util.Properties();

        for (int i = 0; i < attributes.getLength(); i++) {
            p.setProperty(attributes.getQName(i), attributes.getValue(i));
        }
        return p;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean value) {
        success = value;
    }
}
