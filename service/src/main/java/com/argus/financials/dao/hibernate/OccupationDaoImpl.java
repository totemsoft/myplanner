package com.argus.financials.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.argus.dao.SQLHelper;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.dao.ObjectDao;
import com.argus.financials.api.dao.OccupationDao;

public class OccupationDaoImpl implements OccupationDao {

    @Autowired private SQLHelper sqlHelper;

    @Autowired private ObjectDao objectDao;

    /* (non-Javadoc)
     * @see com.argus.financials.dao.OccupationDao#load(com.argus.financials.api.bean.IOccupation, java.sql.ResultSet)
     */
    @Override
    public void load(IOccupation occupation, ResultSet rs) throws SQLException {
        occupation.setId((Integer) rs.getObject("PersonOccupationID"));
        occupation.setJobDescription(rs.getString("JobDescription"));
        occupation.setEmploymentStatusCodeId((Integer) rs.getObject("EmploymentStatusCodeID"));
        occupation.setIndustryCodeId((Integer) rs.getObject("IndustryCodeID"));
        occupation.setOccupationCodeId( (Integer) rs.getObject("OccupationCodeID"));
    }

    /* (non-Javadoc)
     * @see com.argus.financials.dao.OccupationDao#store(com.argus.financials.api.bean.IOccupation, java.sql.Connection)
     */
    @Override
    public int store(IOccupation occupation, Connection con) throws SQLException {
        final Integer personId = occupation.getOwnerId();
        // do insert into Person table
        PreparedStatement sql = con.prepareStatement("INSERT INTO PersonOccupation"
            + " (PersonID,JobDescription,EmploymentStatusCodeID"
            + " ,IndustryCodeID,OccupationCodeID) VALUES (?,?,?,?,?)");
        int i = 0;
        sql.setInt(++i, personId);
        sql.setString(++i, occupation.getJobDescription());
        sql.setObject(++i, occupation.getEmploymentStatusCodeId());
        sql.setObject(++i, occupation.getIndustryCodeId());
        sql.setObject(++i, occupation.getOccupationCodeId());
        sql.executeUpdate();
        int newID = objectDao.getIdentityID(con);
        if (occupation.getId() != null) {
            sql = con.prepareStatement(
                "UPDATE PersonOccupation SET NextID=? WHERE PersonOccupationID=?");
            sql.setInt(1, newID);
            sql.setInt(2, occupation.getId());
            sql.executeUpdate();
        }
        occupation.setId(newID);
        return occupation.getId();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.dao.OccupationDao#find(com.argus.financials.api.bean.IOccupation)
     */
    @Override
    public Integer find(IOccupation occupation) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}