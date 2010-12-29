package com.argus.financials.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.argus.financials.bean.DbConstant;
import com.argus.financials.dao.ClientDao;
import com.argus.financials.domain.hibernate.view.Client;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.PersonName;
import com.argus.util.DateTimeUtils;
import com.argus.util.Range;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
@Repository
public class ClientDaoImpl extends BaseDAOImpl implements ClientDao
{

    /* (non-Javadoc)
     * @see com.argus.financials.dao.ClientDao#findClients(java.util.Map, com.argus.util.Range)
     */
    @SuppressWarnings("unchecked")
    public List<Client> findClients(Map<String, Object> criteria, Range range)
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        String sql = "FROM Client WHERE 1=1";
        if (criteria != null)
        {
            String fn = PersonName.SURNAME;
            if (criteria.containsKey(fn))
            {
                String surname = (String) criteria.get(fn);
                sql += " AND surname LIKE :surname";
                parameters.put("surname", surname + "%");
            }
            fn = PersonName.FIRST_NAME;
            if (criteria.containsKey(fn))
            {
                String firstname = (String) criteria.get(fn);
                sql += " AND firstname LIKE :firstname";
                parameters.put("firstname", firstname + "%");
            }
            fn = PersonName.DATE_OF_BIRTH;
            if (criteria.containsKey(fn))
            {
                String dateOfBirth = (String) criteria.get(fn);
                sql += " AND dateOfBirth = :dateOfBirth";
                parameters.put("dateOfBirth", DateTimeUtils.getDate(dateOfBirth));
            }
            fn = Address.COUNTRY;
            if (criteria.containsKey(fn))
            {
                String countryCode = criteria.get(fn).toString();
                sql += " AND address.countryCodeId = :countryCode";
                parameters.put("countryCode", NumberUtils.createInteger(countryCode));
            }
            fn = Address.STATE;
            if (criteria.containsKey(fn))
            {
                String stateCode = criteria.get(fn).toString();
                sql += " AND address.stateCodeId = :stateCode";
                parameters.put("stateCode", NumberUtils.createInteger(stateCode));
            }
            fn = Address.POSTCODE;
            if (criteria.containsKey(fn))
            {
                String postcode = (String) criteria.get(fn);
                sql += " AND address.postcode = :postcode";
                parameters.put("postcode", NumberUtils.createInteger(postcode));
            }
            if (criteria.containsKey(DbConstant.ALL_USERS_CLIENTS)
                && Boolean.TRUE.equals(criteria.get(DbConstant.ALL_USERS_CLIENTS)))
            {
                // add nothing
            }
            else
            {
                fn = DbConstant.ADVISORID;
                if (criteria.containsKey(fn))
                {
                    String advisorID = criteria.get(fn).toString();
                    sql += " AND ownerId = :ownerId";
                    parameters.put("ownerId", NumberUtils.createInteger(advisorID));
                }
            }
        }
        Query qry = getEntityManager().createQuery(sql);
        updateQuery(qry, parameters);
        updateQuery(qry, range);
        return qry.getResultList();
    }

}