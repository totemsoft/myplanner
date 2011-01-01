package com.argus.financials.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.argus.financials.dao.EntityDao;
import com.argus.financials.domain.hibernate.refdata.Country;

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

}