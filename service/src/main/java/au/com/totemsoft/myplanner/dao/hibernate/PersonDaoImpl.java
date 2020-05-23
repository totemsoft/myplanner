package au.com.totemsoft.myplanner.dao.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import au.com.totemsoft.myplanner.api.bean.PersonName;
import au.com.totemsoft.myplanner.api.dao.EntityDao;
import au.com.totemsoft.myplanner.dao.PersonDao;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Repository
public class PersonDaoImpl extends BaseDAOImpl implements PersonDao {

    @Autowired private EntityDao entityDao;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.dao.PersonDao#load(java.sql.ResultSet, au.com.totemsoft.myplanner.etc.PersonName)
     */
    @Override
    public void load(ResultSet rs, PersonName personName) throws SQLException {
        Number sexCodeId = (Number) rs.getObject("SexCodeID");
        personName.setSex(sexCodeId == null ? null : entityDao.findSexCode(sexCodeId.intValue()));
        Number titleCodeId = (Number) rs.getObject("TitleCodeID");
        personName.setTitle(titleCodeId == null ? null : entityDao.findTitleCode(titleCodeId.intValue()));
        Number maritalCodeId = (Number) rs.getObject("MaritalCodeID");
        personName.setMarital(maritalCodeId == null ? null : entityDao.findMaritalCode(maritalCodeId.intValue()));
        personName.setSurname(rs.getString("FamilyName"));
        personName.setFirstname(rs.getString("FirstName"));
        personName.setOtherNames(rs.getString("OtherGivenNames"));
        personName.setPreferredName(rs.getString("PreferredName"));
        personName.setDateOfBirth(rs.getDate("DateOfBirth"));
        personName.setModified(false);
    }

}