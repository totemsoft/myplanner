package com.argus.financials.dao.hibernate;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.argus.financials.dao.EntityDao;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.State;
import com.argus.financials.domain.refdata.ICountry;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
@Repository
public class EntityDaoImpl extends BaseDAOImpl implements EntityDao
{

    /* (non-Javadoc)
     * @see com.argus.financials.dao.EntityDao#findCountries()
     */
    @SuppressWarnings("unchecked")
    public List<Country> findCountries()
    {
        return getEntityManager()
            .createQuery("FROM Country ORDER BY description")
            .getResultList();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.dao.EntityDao#findStates(java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    public List<State> findStates(Integer countryId)
    {
        if (ICountry.AUSTRALIA_ID.equals(countryId))
        {
            return getEntityManager()
                .createQuery("FROM State ORDER BY description")
                //.setParameter("countryId", countryId) // TODO: add countryId column
                .getResultList();
        }
        return Collections.emptyList();
    }

}