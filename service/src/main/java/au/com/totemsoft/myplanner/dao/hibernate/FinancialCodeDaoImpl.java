package au.com.totemsoft.myplanner.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialType;
import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.api.dao.FinancialCodeDao;
import au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsTableRow;

@Repository
public class FinancialCodeDaoImpl extends BaseDAOImpl implements FinancialCodeDao {

    @Autowired private SQLHelper sqlHelper;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.dao.FinancialCodeDao#findFinancialTypes(java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<FinancialType> findFinancialTypes(Integer objectTypeId) {
        return getEntityManager()
            .createNamedQuery("financialType.findByObjectType")
            .setParameter("objectTypeId", objectTypeId)
            .getResultList();
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.dao.FinancialCodeDao#findFinancialType(java.lang.Integer)
     */
    @Override
    public FinancialType findFinancialType(Integer financialTypeId) {
        return findById(FinancialType.class, financialTypeId);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.dao.FinancialCodeDao#findFinancialCode(java.lang.Integer)
     */
    @Override
    public FinancialCode findFinancialCode(Integer financialCodeId) {
        return findById(FinancialCode.class, financialCodeId);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.dao.FinancialCodeDao#findByColumnName(java.lang.String, java.lang.String)
     */
    @Override
    public FinancialCode findByColumnName(String column_name, String id) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("[" + FinancialCode.TABLE_NAME + "] ");
        sql.append("WHERE [" + column_name + "] = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            FinancialCode result = null;
            if (rs.next()) {
                result = new FinancialCode();
                result.setId(rs.getInt("FinancialCodeID"));
                result.setFinancialTypeId(rs.getInt("FinancialTypeID"));
                result.setCode(rs.getString("FinancialCode"));
                result.setDescription(rs.getString("FinancialCodeDesc"));
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.dao.FinancialCodeDao#findByKeywords(java.lang.String, java.lang.String)
     */
    @Override
    public Vector findByKeywords(String keywords, String column_name) throws SQLException {
        // check if we have some keywords
        Vector result = new Vector();
        if (StringUtils.isBlank(keywords) || StringUtils.isBlank(column_name)) {
            return result;
        }
        // split keywords String into tokens (single keywords)
        final StringTokenizer tok = new StringTokenizer(keywords);
        final int number_of_tokens = tok.countTokens();
        int token_count = 0;
        column_name = column_name.trim();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ");
        sql.append("[FinancialCode], [FinancialCodeDesc] ");
        sql.append("FROM [" + FinancialCode.TABLE_NAME + "] ");
        sql.append("WHERE (");
        while (tok.hasMoreTokens()) {
            // do we need to add "AND "?
            if (token_count > 0 && token_count < number_of_tokens) {
                sql.append("OR ");
            }
            sql.append("[" + column_name + "] LIKE '%" + tok.nextToken() + "%' ");
            token_count++;
        }
        // select only user created FinancialCodes, they start with '#'
        sql.append(" OR [FinancialCode] LIKE '#%'");
        sql.append(") ");
        sql.append(" AND LogicallyDeleted IS NULL ");
        sql.append("ORDER BY [" + column_name + "] ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // create new row
                AvailableInvestmentsTableRow table_row = new AvailableInvestmentsTableRow();
                // fill it with data
                table_row.origin = checkString(FinancialCode.TABLE_NAME);
                table_row.investmentCode = checkString(rs.getString(FinancialCode.COLUMN_CODE));
                table_row.description = checkString(rs.getString(FinancialCode.COLUMN_DESC));
                table_row.code = "";
                table_row.institution = "";
                // store row
                result.add(table_row);
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    @Override
    public void create(FinancialCode entity) throws SQLException {
        Integer financialTypeId = entity.getFinancialTypeId();
        int financialCodeId = 0;
        try (Connection con = sqlHelper.getConnection();) {
            // get max. FinancialCodeID
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT MAX(FinancialCodeID) ");
            sql.append("FROM ");
            sql.append("[" + FinancialCode.TABLE_NAME + "] ");
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());
                    ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    financialCodeId = rs.getInt(1);
                    // create new "unique" FinancialCodeId
                    financialCodeId++;
                }
            }

            sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append("[" + FinancialCode.TABLE_NAME + "] ");
            sql.append("(FinancialCodeID, FinancialTypeID, FinancialCode, FinancialCodeDesc) ");
            sql.append("VALUES ( ?, ?, ?, ? )");
            try (PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
                pstmt.setInt(1, financialCodeId);
                // pstmt.setInt (2, this.financialTypeId); // should already
                // exists in FinancialType table
                if (financialTypeId != null && financialTypeId > 0) {
                    pstmt.setInt(2, financialTypeId);
                } else {
                    pstmt.setInt(2, FinancialTypeEnum.UNDEFINED.getId());
                }
                pstmt.setString(3, entity.getCode());
                pstmt.setString(4, entity.getDescription());
                /*int status = */pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.dao.FinancialCodeDao#store(au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode)
     */
    @Override
    public void store(FinancialCode entity) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("[" + FinancialCode.TABLE_NAME + "] ");
        sql.append("SET ");
        sql.append("FinancialTypeID = ?, ");
        sql.append("FinancialCode = ?, ");
        sql.append("FinancialCodeDesc = ? ");
        sql.append("WHERE FinancialCodeID = ? ");
        try (Connection con = sqlHelper.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
            Integer financialTypeId = entity.getFinancialType().getId();
            if (financialTypeId > 0) {
                pstmt.setInt(1, financialTypeId);
            } else {
                pstmt.setInt(1, FinancialTypeEnum.UNDEFINED.getId());
            }
            pstmt.setString(2, entity.getCode());
            pstmt.setString(3, entity.getDescription());
            pstmt.setInt(4, entity.getId());
            int count = pstmt.executeUpdate();
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw e;
        }
    }

    /**
     * Check if the given String is null, if so it creates a new String which
     * contains "". If the given String was not null, it creates a new String
     * with the contents of the given one.
     * 
     * @return a new String
     */
    private String checkString(String str) {
        return str != null ? str : "";
    }

}