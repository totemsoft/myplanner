/*
 * UtilityServiceImpl.java
 *
 * Created on 16 August 2001, 18:33
 */

package au.com.totemsoft.myplanner.service.impl;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.io.IOUtils2;
import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.code.CodeComparator;
import au.com.totemsoft.myplanner.code.Code;
import au.com.totemsoft.myplanner.code.SexCode;
import au.com.totemsoft.myplanner.dao.hibernate.AbstractSQLHelper;
import au.com.totemsoft.myplanner.etc.Contact;
import au.com.totemsoft.myplanner.etc.db.ContactBean;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.projection.save.db.ModelBean;
import au.com.totemsoft.myplanner.service.UtilityService;
import au.com.totemsoft.myplanner.strategy.StrategyGroup;
import au.com.totemsoft.myplanner.strategy.db.StrategyGroupBean;
import au.com.totemsoft.swing.SplashWindow;
import au.com.totemsoft.util.ReferenceCode;
import au.com.totemsoft.util.StringUtils;

public class UtilityServiceImpl implements UtilityService {

    private static final Logger LOG = Logger.getLogger(UtilityServiceImpl.class);

    @Autowired private SQLHelper sqlHelper;

    public List<Contact> findUsers() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            Vector<Contact> data = null;
            String sql = "SELECT UserPersonID FROM UserPerson";
            try (PreparedStatement pstm = con.prepareStatement(sql);
                    ResultSet rs = pstm.executeQuery();) {
                while (rs.next()) {
                    if (data == null)
                        data = new Vector<Contact>();
                    Contact c = new Contact();
                    c.setId((Integer) rs.getObject(1)); // UserPersonID
                    data.addElement(c);
                }
            }

            if (data == null)
                return null;

            Iterator<Contact> iter = data.iterator();
            ArrayList<Contact> toRemove = new ArrayList<Contact>();
            while (iter.hasNext()) {
                Contact c = (Contact) iter.next();
                new ContactBean(c).load(con);
                if (c.toString().trim().length() == 0) {
                    //System.err.println("UserServiceImpl::findUsers(), c.toString().trim().length() == 0 for ID:" + c.getId());
                    // mark for remove
                    toRemove.add(c);
                }
            }
            iter = toRemove.iterator();
            while (iter.hasNext()) {
                Contact c = iter.next();
                data.remove(c);
            }
            return data;
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 
     */
    public java.util.TreeMap getCodes(String tableName) throws ServiceException {
        return getCodes(tableName, tableName + "ID", tableName + "Desc");
    }

    public java.util.TreeMap getCodes(String tableName, String fieldKeyValues,
            String fieldKey) throws ServiceException {
        String sql = "SELECT " + fieldKey + ", " + fieldKeyValues + " FROM " + tableName;
        try (Connection con = sqlHelper.getConnection();
                Statement pstm = con.createStatement();
                ResultSet rs = pstm.executeQuery(sql);) {
            java.util.TreeMap map = null;
            while (rs.next()) {
                if (map == null)
                    map = new java.util.TreeMap();

                String key = rs.getString(fieldKey).trim();
                Object values = getValues(rs, fieldKeyValues);
                map.put(key, values);
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    private Object getValues(ResultSet rs, String fieldKeyValues)
            throws SQLException {

        if (fieldKeyValues.indexOf(',') < 0)
            return rs.getObject(fieldKeyValues);

        Vector values = new Vector();
        java.util.StringTokenizer st = new java.util.StringTokenizer(
                fieldKeyValues, ",");
        while (st.hasMoreTokens())
            values.addElement(rs.getObject(st.nextToken().trim()));
        return values;

    }

    /**
     * Map( StateCodeID, Object [] { postCode2Suburbs = Map( PostCode,
     * List(Suburb) ), suburb2PostCodes = Map( Suburb, List(PostCode) ) } )
     */
    public java.util.Map getPostCodes(Integer countryCodeID)
            throws ServiceException {
        int stateCodeID = 0;
        String postCode = null;
        String suburb = null;
        java.util.Map map = new java.util.TreeMap();
        java.util.Map postCode2Suburbs = null;
        java.util.Map suburb2PostCodes = null;
        try (Connection con = sqlHelper.getConnection();) {
            // 1. postCode2Suburbs
            String sql = "SELECT StateCodeID, PostCode, Suburb"
                + " FROM SuburbPostCode WHERE CountryCodeID=?"
                + " ORDER BY StateCodeID, PostCode, Suburb";
            try (PreparedStatement pstm = con.prepareStatement(sql);) {
                pstm.setInt(1, countryCodeID.intValue());
                try (ResultSet rs = pstm.executeQuery();) {
                    java.util.List suburbs = null;
                    while (rs.next()) {
                        int _stateCodeID = rs.getInt(1);
                        String _postCode = rs.getString(2).trim();
                        String _suburb = rs.getString(3).trim();
                        // new state, has distinct postcodes
                        if (stateCodeID != _stateCodeID) {
                            // next state group (order by state)
                            stateCodeID = _stateCodeID;
                            postCode = _postCode;
                            postCode2Suburbs = new java.util.TreeMap();
                            postCode2Suburbs.put(Code.NONE, null);
                            suburbs = new java.util.ArrayList();
                            postCode2Suburbs.put(postCode, suburbs);
                            suburbs.add(Code.NONE);
                            map.put(new Integer(_stateCodeID), new Object[] {
                                    postCode2Suburbs, new java.util.TreeMap() });
                        } else {
                            // postCode2Suburbs = Map( PostCode, List(Suburb) ordered by postcode
                            if (!_postCode.equals(postCode)) {
                                postCode = _postCode;
                                suburbs = new java.util.ArrayList();
                                postCode2Suburbs.put(postCode, suburbs);
                                suburbs.add(Code.NONE);
                            }
                        }
                        suburbs.add(_suburb);
                    }
                }
            }

            stateCodeID = 0;

            // 2. suburb2PostCodes
            sql = "SELECT StateCodeID, Suburb, PostCode"
                    + " FROM SuburbPostCode WHERE CountryCodeID=?"
                    + " ORDER BY StateCodeID, Suburb, PostCode";
            try (PreparedStatement pstm = con.prepareStatement(sql);) {
                pstm.setInt(1, countryCodeID.intValue());
                try (ResultSet rs = pstm.executeQuery();) {
                    java.util.List postCodes = null;
                    while (rs.next()) {
                        int _stateCodeID = rs.getInt(1);
                        String _suburb = rs.getString(2).trim();
                        String _postCode = rs.getString(3).trim();
                        // new state, has distinct postcodes
                        if (stateCodeID != _stateCodeID) {
                            // next state group (order by state)
                            stateCodeID = _stateCodeID;
                            suburb = _suburb;
                            suburb2PostCodes = (java.util.TreeMap) ((Object[]) map
                                    .get(new Integer(stateCodeID)))[1];
                            suburb2PostCodes.put(Code.NONE, null);
                            postCodes = new java.util.ArrayList();
                            suburb2PostCodes.put(_suburb, postCodes);
                            postCodes.add(Code.NONE);
                        } else {
                            // suburb2PostCodes = Map( Suburb, List(PostCode) ordered by Suburb
                            if (!_suburb.equals(suburb)) {
                                suburb = _suburb;
                                postCodes = new java.util.ArrayList();
                                suburb2PostCodes.put(_suburb, postCodes);
                                postCodes.add(Code.NONE);
                            }
                        }
                        postCodes.add(_postCode);
                    }
                }
            }
            return map;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * CONTAINER structure Map( objType, Map( ReferenceCode( finTypeID,
     * finTypeDesc ), Vector( ReferenceCode( finCodeID, finCode, finCodeDesc ) ) ) )
     */
    public java.util.Map getFinancialObjectTypes() throws ServiceException {
        String sql = "SELECT"
            + " ft.ObjectTypeID, ft.FinancialTypeID, ft.FinancialTypeDesc,"
            + " fc.FinancialCodeID, fc.FinancialCode, fc.FinancialCodeDesc"
            + " FROM FinancialType ft"
            + " LEFT OUTER JOIN FinancialCode fc"
            + " ON ft.FinancialTypeID = fc.FinancialTypeID"
            + " ORDER BY ft.ObjectTypeID, ft.FinancialTypeID, fc.FinancialCodeID";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstm = con.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery();) {
            java.util.Map result = null;
            java.util.Map finTypes = null;
            java.util.Vector finCodes = null;
            // this record
            Integer objTypeID = null;
            Integer finTypeID = null;
            String finTypeDesc = null;
            // prev record
            Integer prevObjTypeID = null;
            Integer prevFinTypeID = null;
            while (rs.next()) {
                objTypeID = (Integer) rs.getObject("ObjectTypeID");
                finTypeID = (Integer) rs.getObject("FinancialTypeID");
                finTypeDesc = rs.getString("FinancialTypeDesc");
                finTypeDesc = finTypeDesc == null ? null : finTypeDesc.trim();
                // update 135.sql (added FinancialObjectTypeID.UNDEFINED)
                // FinancialTypeID in FinancialCode table can be null
                // if ( finTypeID == null ) {
                // finTypeID = new Integer(0);
                // objTypeID = new Integer(0);
                // }
                if (result == null) {
                    result = new TreeMap(new CodeComparator());
                    prevObjTypeID = objTypeID;
                    prevFinTypeID = finTypeID;
                }
                // reset these maps if we moved to next object type/fin type
                if (!objTypeID.equals(prevObjTypeID))
                    finTypes = null;
                if (!finTypeID.equals(prevFinTypeID))
                    finCodes = null;
                // .. and create new ones
                if (finTypes == null) {
                    finTypes = new java.util.TreeMap(new CodeComparator());
                    result.put(objTypeID, finTypes);
                    // add null fin. type entry
                    finTypes.put(ReferenceCode.CODE_NONE, new java.util.Vector());// null );
                }
                if (finCodes == null) {
                    finCodes = new java.util.Vector();
                    finTypes.put(new ReferenceCode(finTypeID, finTypeDesc), finCodes);
                    // add null fin. code entry
                    finCodes.add(ReferenceCode.CODE_NONE);
                }
                // load finCodes entry with new finCodes
                int finCodeID = rs.getInt("FinancialCodeID");
                String finCode = rs.getString("FinancialCode");
                finCode = finCode == null ? null : finCode.trim();
                String finCodeDesc = rs.getString("FinancialCodeDesc");
                finCodeDesc = finCodeDesc == null ? null : finCodeDesc.trim();
                finCodes.add(new ReferenceCode(finCodeID, finCode, finCodeDesc));
                // update prev value
                prevFinTypeID = finTypeID;
                prevObjTypeID = objTypeID;
            }
            return result;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public java.util.HashMap getLifeExpectancy(Integer countryCodeID)
            throws ServiceException {
        String sql = "SELECT Age, SexCodeID, LEValue"
            + " FROM LifeExpectancy" + " WHERE CountryCodeID = ?"
            + " ORDER BY Age, SexCodeID";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstm = con.prepareStatement(sql);) {
            pstm.setInt(1, countryCodeID.intValue());
            try (ResultSet rs = pstm.executeQuery();) {
                java.util.HashMap map = null;
                Integer age = null;
                double femaleLE = 0; // SexCode.FEMALE = 1
                double maleLE = 0; // SexCode.MALE = 2
                int count = 0;
                while (rs.next()) {
                    if (map == null)
                        map = new java.util.HashMap();
                    age = (Integer) rs.getObject("Age");
                    if (rs.getObject("SexCodeID").equals(SexCode.FEMALE))
                        femaleLE = rs.getDouble("LEValue");
                    else
                        maleLE = rs.getDouble("LEValue");
                    if (femaleLE != 0 && maleLE != 0) {
                        map.put(age, new double[] { femaleLE, maleLE });
                        femaleLE = 0;
                        maleLE = 0;
                        count = 0;
                    } else
                    // check that are only TWO entries for this age (female and male)
                    if (++count > 2)
                        throw new RuntimeException(
                            "Only TWO entries for this age are allowed (female and male): " + age.intValue());
                }
                return map;
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public java.util.HashMap getParameters(Integer paramTypeID) throws ServiceException {
        String sql;
        if (paramTypeID == null)
            sql = "SELECT ParamName, ParamValue FROM Params ORDER BY ParamName";
        else
            sql = "SELECT ParamName, ParamValue FROM Params WHERE ParamTypeID = ? ORDER BY ParamName";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstm = con.prepareStatement(sql);) {
            if (paramTypeID != null)
                pstm.setInt(1, paramTypeID.intValue());
            try (ResultSet rs = pstm.executeQuery();) {
                java.util.HashMap map = null;
                while (rs.next()) {
                    if (map == null)
                        map = new java.util.HashMap();
                    map.put(rs.getString("ParamName"), rs.getString("ParamValue"));
                }
                return map;
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * TODO: virtual method, move to SQLServerHelper, etc
     */
    public String getDBServerVersion() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            try (PreparedStatement pstm = con.prepareStatement("SELECT @@VERSION");
                    ResultSet rs = pstm.executeQuery();) {
                String dbServerVersion;
                if (rs.next())
                    dbServerVersion = rs.getString(1);
                else
                    dbServerVersion = "0"; // no version ??? , will never happened
                return dbServerVersion;
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /***************************************************************************
     * TODO: move it to ConnectionPool
     */
    public String getDBVersion() throws ServiceException {
        try (Connection con = sqlHelper.getConnection();) {
            String sql = "SELECT CurrentVersion FROM DBVersion"
                + " ORDER BY CurrentVersion DESC"; // most recent first
                //+ " ORDER BY DateCreated DESC"; // most recent first
            try (PreparedStatement pstm = con.prepareStatement(sql);
                    ResultSet rs = pstm.executeQuery();) {
                String dbVersion;
                if (rs.next())
                    dbVersion = rs.getString(1);
                else
                    dbVersion = "___???___"; // no version ??? , will never happened
                return dbVersion;
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.UtilityService#syncDBSchema()
     */
    public void syncDBSchema() throws Exception
    {
        String dbVersion = getDBVersion();
        syncDBSchema(dbVersion, REQUIRED_DBVERSION);
    }

    public void syncDBSchema(String currVersion, String reqVersion) throws Exception {
        int curr = getFullVersionID(currVersion);
        int req = getFullVersionID(reqVersion);
        
        // create splash window
        SplashWindow splash = null;
        String update = null;
        try
        {
            splash = new SplashWindow("/image/financial-planner.jpg");
            new Thread( splash, "SplashWindowThread" ).start();
        }
        catch (Exception ignore)
        {
            LOG.warn(ignore.getMessage());
        }
        try {
            String updateDir = curr < 200 ? "data/updates/core" : "data/updates";
            Connection con = sqlHelper.getConnection();
            for (int i = curr + 1; i <= req; i++) {
                update = updateDir + "/" + i + ".sql";
                i = syncDBSchema(con, i, update);
            } 
            con.close();
        } finally {
            if (splash != null) {
                splash.setStringPainted("Successfully completed: " + update);
                splash.dispose();
            }
        }
    }

    public int syncDBSchema(Connection con, int i, String update) throws Exception {
        List<String> list = AbstractSQLHelper.parse(update);

        LOG.info("Preparing to run update script: " + update);

        // add to Map, send to execute
        String sql = null;
        try {
            for (int j = 0; j < list.size(); j++) {
                sql = list.get(j);
                LOG.info(sql);
                try (Statement stmt = con.createStatement();) {
                    int count = stmt.executeUpdate(sql);
                }
            }

            // //////////////////////////////////////////////////////////////////////////////
            // !!! IF YOU NEED TO RUN SOME JAVA CODE AFTER SQL UPDATE, DO IT
            // HERE!!! //
            // //////////////////////////////////////////////////////////////////////////////
            if (i == 0) // after 0.sql
                ;
            else if (i == 13)
                i = 109; // in update 00.12 we jump to 01.10 (increment major version)
            else if (i == 163) // after 163.sql
                updateAfter163(con);
            else if (i == 166)
                i = 199; // in update 01.67 we jump to 02.00 (increment major version)
            // //////////////////////////////////////////////////////////////////////////////
            // !!! IF YOU NEED TO RUN SOME JAVA CODE AFTER SQL UPDATE, DO IT
            // HERE!!! //
            // //////////////////////////////////////////////////////////////////////////////

        } catch (Exception e) {
            LOG.error("\tUtilityBean::syncDBSchema(...) FAILED for:\n" + sql + "\n" + e.getMessage());
            if (i == 4) {// after 4.sql
                updateAfter4(con);
            } else if (i == 162) {// after 162.sql
                //export for MSSQL only
            } else if (i == 163) {// after 163.sql
                //import for MSSQL only
            } else if (i == 165) {// after 165.sql
                //TODO: fix 4 HSQLDB
                //intensive use of sroreproc (Journal???) for MSSQL only
            } else if (i == 166) {// after 166.sql
                i = 199;
                //import for MSSQL only
            } else {
                throw new ServiceException(e);
            }
            LOG.error("Recovered from error!");
        }
        LOG.info("\tSuccessfully completed: " + update);
        return i;
    }

    private String fromText(String data) {
        try {
            return new String(StringUtils.fromHexString(data),
                    IOUtils2.ENCODING_2_SERIALIZE);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private String toText(String data) {
        try {
            return StringUtils.toHexString(data
                    .getBytes(IOUtils2.ENCODING_2_SERIALIZE));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private void updateAfter4(Connection con) throws SQLException {
        String sql = "INSERT INTO Object (ObjectTypeID) VALUES (0)";
        Statement stmt = con.createStatement();
        try {
            int count = 10000;
            while (count-- > 0) {
                stmt.addBatch(sql);
            }
            int[] res = stmt.executeBatch();
            //for (int i = 0; i < res.length; i++)
            //    System.out.println(i + "). " + res[i]);
        } finally {
            stmt.close();
        }
    }

    private void updateAfter163(Connection con) throws SQLException {

        java.util.Collection list = new java.util.ArrayList();

        // //////////////////////////////////////////////////////////////////////
        // get all strategies //
        // //////////////////////////////////////////////////////////////////////
        StrategyGroupBean sgb = new StrategyGroupBean();

        String sql = "SELECT " + sgb.getSelectFieldsList()
            + " FROM StrategyGroup WHERE StrategyGroupData IS NOT NULL AND StrategyGroupData2 IS NULL";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            while (rs.next()) {
                StrategyGroup sg = new StrategyGroup();
                sgb.setStrategyGroup(sg);
                sgb.load(rs);
                list.add(sg);
            }
        }

        // //////////////////////////////////////////////////////////////////////
        // get all models //
        // //////////////////////////////////////////////////////////////////////
        ModelBean mb = new ModelBean();

        sql = "SELECT " + mb.getSelectFieldsList()
            + " FROM Model WHERE ModelData IS NOT NULL AND ModelData2 IS NULL";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            while (rs.next()) {
                Model m = new Model();
                mb.setModel(m);
                mb.load(rs);
                list.add(m);
            }
        }

        // //////////////////////////////////////////////////////////////////////
        // get all plans //
        // //////////////////////////////////////////////////////////////////////
        sql = "SELECT PlanDataID, PlanDataText, PlanDataText2 FROM PlanData";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            while (rs.next()) {
                int i = 0;
                int planDataID = rs.getInt(++i);
                String planDataText = rs.getString(++i);
                String planDataText2 = rs.getString(++i); // new hex
                                                            // presentation of
                                                            // text data
                if (planDataText2 == null)
                    planDataText2 = toText("" + planDataText);
                String s = "UPDATE PlanData SET" + " PlanDataText2='"
                        + planDataText2 + "'" + " WHERE PlanDataID="
                        + planDataID;
                list.add(s);
            }
        }

        // //////////////////////////////////////////////////////////////////////
        // update strategies/models //
        // //////////////////////////////////////////////////////////////////////
        java.util.Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof Model) {
                mb.setModel((Model) o);
                mb.store(con);
            } else if (o instanceof StrategyGroup) {
                sgb.setStrategyGroup((StrategyGroup) o);
                sgb.store(con);
            } else if (o instanceof java.lang.String) {
                try (Statement stmt = con.createStatement();) {
                    stmt.executeUpdate(o.toString());
                }
            } else {
                System.err.println("UtilityServiceImpl::updateAfter163()\tUnhandled class: " + o);
            }
        }
    }

    public Integer addCode(String tableName, String codeValue)
            throws ServiceException,
            au.com.totemsoft.myplanner.api.InvalidCodeException {
        return addCode(tableName, tableName + "ID", tableName + "Desc",
                codeValue);
    }

    public Integer addCode(String tableName, String idFieldName,
            String descFieldName, String codeValue) throws ServiceException, InvalidCodeException {
        Integer codeID = addCode2Master(tableName, idFieldName, descFieldName, codeValue);
        if (codeID == null)
            throw new au.com.totemsoft.myplanner.api.InvalidCodeException(
                "Invalid Code: " + tableName + idFieldName + descFieldName + codeValue);

        int count = 0;
        try (Connection con = sqlHelper.getConnection();) {
            String sql = "SELECT count(*) FROM " + tableName + " WHERE " + descFieldName + " LIKE ?";
            try (PreparedStatement pstm = con.prepareStatement(sql);) {
                pstm.setString(1, codeValue);
                try (ResultSet rs = pstm.executeQuery();) {
                    rs.next();
                    count = rs.getInt(1);
                }
            }
            if (count == 0) {
                sql = "INSERT INTO " + tableName + " ( " + idFieldName + ", " + descFieldName + " ) VALUES ( ?, ? )";
                try (PreparedStatement pstm = con.prepareStatement(sql);) {
                    pstm.setInt(1, codeID.intValue());
                    pstm.setString(2, codeValue);
                    count = pstm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (count != 1)
            throw new au.com.totemsoft.myplanner.api.InvalidCodeException(
                "FAILED to add new code.\n" + tableName + ", "
                    + idFieldName + ", " + descFieldName + ", "
                    + codeValue + ".");
        return codeID;
    }

    // add new codeID in web database (master)
    private Integer addCode2Master(String tableName, String idFieldName,
            String descFieldName, String codeValue) throws ServiceException {
        Integer codeID = null;
        // SELECT max( " + idFieldName + " ) + 1
        return codeID;
    }

    private int getFullVersionID(String version) {
        if (version == null)
            return 0;
        String major = version.substring(version.indexOf(".") + 1, version
                .lastIndexOf("."));
        String minor = version.substring(version.lastIndexOf(".") + 1);

        return Integer.parseInt(major) * 100 + Integer.parseInt(minor);

    }

    public void addCode(String tableName, java.util.Map addData) throws ServiceException {
        String names = null;
        String values = null;
        java.util.Iterator iter = addData.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            if (names == null)
                names = "";
            else
                names += ",";
            names += entry.getKey().toString();
            if (values == null)
                values = "";
            else
                values += ",";
            values += "?";
        }
        String sql = "INSERT INTO " + tableName + " ( " + names + " ) VALUES ( " + values + " )";
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstm = con.prepareStatement(sql);) {
            Object[] values2 = addData.values().toArray();
            for (int i = 0; i < values2.length; i++)
                pstm.setObject(i + 1, values2[i]);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateCode(String tableName, java.util.Map updateData, java.util.Map whereData) throws ServiceException {
        String values = null;
        java.util.Iterator iter = updateData.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            if (values == null)
                values = "";
            else
                values += ",";
            values += entry.getKey().toString() + "=?";
        }
        String where = null;
        iter = whereData.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            if (where == null)
                where = "";
            else
                where += " AND ";
            where += entry.getKey().toString() + "=?";
        }
        String sql = "UPDATE " + tableName + " SET " + values + " WHERE " + where;
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstm = con.prepareStatement(sql);) {
            Object[] values2 = updateData.values().toArray();
            int j = 1;
            for (int i = 0; i < values2.length; i++)
                pstm.setObject(j++, values2[i]);
            values2 = whereData.values().toArray();
            for (int i = 0; i < values2.length; i++)
                pstm.setObject(j++, values2[i]);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    // implemented in AbstractPersistable
    // public String getDatabaseURL() throws ServiceException;

}
